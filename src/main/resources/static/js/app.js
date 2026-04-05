const imageGrid = document.getElementById('image-grid');
const emptyState = document.getElementById('empty-state');
const fileInput = document.getElementById('file-input');
const fileSelectionNode = document.getElementById('file-selection');
const uploadButton = document.getElementById('upload-button');
const statusNode = document.getElementById('status');
const imageCountNode = document.getElementById('image-count');

function setStatus(message, isError = false) {
    statusNode.textContent = message;
    statusNode.classList.toggle('error', isError);
}

function formatFileSelection(file) {
    if (!file) {
        return 'JPG, PNG or WEBP';
    }

    const sizeInMb = file.size / (1024 * 1024);
    return `${file.name} · ${sizeInMb.toFixed(2)} MB`;
}

function updateFileSelection() {
    if (!fileSelectionNode) {
        return;
    }

    fileSelectionNode.textContent = formatFileSelection(fileInput?.files?.[0]);
}

function updateImageCount() {
    const count = imageGrid ? imageGrid.children.length : 0;
    imageCountNode.textContent = `Images: ${count}`;
    const hasImages = count > 0;

    if (imageGrid) {
        imageGrid.hidden = !hasImages;
    }

    if (emptyState) {
        emptyState.hidden = hasImages;
    }
}

function createImageCard(image) {
    const card = document.createElement('article');
    card.className = 'card';
    card.dataset.imageId = image.id;
    card.innerHTML = `
        <img class="thumb" src="${image.url}" alt="${image.fileName}">
        <div class="card-body">
            <p class="file-name"></p>
            <p class="type"></p>
            <div class="card-actions">
                <a class="button" target="_blank" rel="noreferrer">Open</a>
                <button class="delete-button" type="button">Delete</button>
            </div>
        </div>
    `;

    card.querySelector('.file-name').textContent = image.fileName;
    card.querySelector('.type').textContent = image.contentType;
    const openLink = card.querySelector('a');
    openLink.href = image.url;
    const deleteButton = card.querySelector('.delete-button');
    deleteButton.dataset.imageId = image.id;

    return card;
}

const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
const csrfHeaderName = document.querySelector('meta[name="_csrf_header"]')?.content;
const accessToken = document.querySelector('meta[name="access_token"]')?.content;

function withCsrfHeaders(headers = {}, method = 'GET') {
    const nextHeaders = {...headers};

    if (csrfToken && csrfHeaderName && method !== 'GET' && method !== 'HEAD' && method !== 'OPTIONS') {
        nextHeaders[csrfHeaderName] = csrfToken;
    }

    return nextHeaders;
}

function withAuthHeaders(headers = {}) {
    const nextHeaders = {...headers};

    if (accessToken) {
        nextHeaders.Authorization = `Bearer ${accessToken}`;
    }

    return nextHeaders;
}

function getUploadContentType(file) {
    const name = file?.name?.toLowerCase() || '';
    if (name.endsWith('.jpg') || name.endsWith('.jpeg')) {
        return 'image/jpg';
    }
    if (name.endsWith('.png')) {
        return 'image/png';
    }
    if (name.endsWith('.webp')) {
        return 'image/webp';
    }
    return file?.type || 'application/octet-stream';
}

function debugUploadRequest(uploadUrl, contentType, file) {
    console.debug('S3 upload request', {
        method: 'PUT',
        url: uploadUrl,
        headers: {
            'Content-Type': contentType
        },
        fileName: file?.name,
        fileSize: file?.size
    });
}

async function requestJson(url, options) {
    const method = options?.method || 'GET';
    const headers = withAuthHeaders(withCsrfHeaders(
        options && options.headers ? options.headers : {},
        method
    ));

    const response = await fetch(url, {
        ...options,
        headers
    });

    if (!response.ok) {
        const text = await response.text();
        throw new Error(text || `Request failed with ${response.status}`);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

async function uploadSelectedFile() {
    const file = fileInput.files[0];
    if (!file) {
        setStatus('Choose an image first.', true);
        return;
    }

    uploadButton.disabled = true;
    setStatus(`Uploading ${file.name}...`);

    try {
        const contentType = getUploadContentType(file);
        const uploadUrlResponse = await requestJson('/images/upload-url', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                fileName: file.name,
                contentType
            })
        });

        let uploadResponse;
        try {
            const imageBytes = await file.arrayBuffer();
            debugUploadRequest(uploadUrlResponse.uploadUrl, contentType, file);
            uploadResponse = await fetch(uploadUrlResponse.uploadUrl, {
                method: 'PUT',
                headers: {
                    'Content-Type': contentType
                },
                body: imageBytes
            });
        } catch (error) {
            throw new Error('Upload to storage failed before reaching the server. Check bucket CORS for PUT from this app origin.');
        }

        if (!uploadResponse.ok) {
            const text = await uploadResponse.text();
            console.error('S3 upload failed', {
                status: uploadResponse.status,
                statusText: uploadResponse.statusText,
                body: text
            });
            throw new Error(text || `Upload failed with ${uploadResponse.status}`);
        }

        const image = await requestJson('/images/confirm', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({key: uploadUrlResponse.key})
        });

        const refreshed = await requestJson('/images', {method: 'GET', headers: {}});
        const newImage = refreshed.images.find(candidate => candidate.id === image.id);
        if (!newImage) {
            throw new Error('Upload completed but the image could not be reloaded.');
        }

        imageGrid.prepend(createImageCard(newImage));
        fileInput.value = '';
        updateFileSelection();
        updateImageCount();
        setStatus(`${newImage.fileName} uploaded.`);
    } catch (error) {
        setStatus(error.message || 'Upload failed.', true);
    } finally {
        uploadButton.disabled = false;
    }
}

async function deleteImage(imageId, button) {
    button.disabled = true;
    setStatus('Deleting image...');

    try {
        await requestJson(`/images/${imageId}`, {
            method: 'DELETE',
            headers: {}
        });

        const card = button.closest('.card');
        if (card) {
            card.remove();
        }

        updateImageCount();
        setStatus('Image deleted.');
    } catch (error) {
        button.disabled = false;
        setStatus(error.message || 'Delete failed.', true);
    }
}

uploadButton.addEventListener('click', uploadSelectedFile);
fileInput?.addEventListener('change', updateFileSelection);

imageGrid?.addEventListener('click', event => {
    const button = event.target.closest('.delete-button');
    if (!button) {
        return;
    }

    deleteImage(button.dataset.imageId, button);
});

updateImageCount();
updateFileSelection();
