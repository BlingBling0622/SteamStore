// Game Detail Page JavaScript - Media Carousel & Interactions

let currentMediaIndex = 0;
let mediaItems = [];

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    initMediaCarousel();
    loadThumbnails();
});

// Initialize media carousel
function initMediaCarousel() {
    const mainImage = document.getElementById('mainImage');
    const mainVideo = document.getElementById('mainVideo');

    // Collect all media items (video + screenshots)
    if (mainVideo && mainVideo.querySelector('source')) {
        mediaItems.push({
            type: 'video',
            src: mainVideo.querySelector('source').src,
            element: mainVideo
        });
    }

    // Add image as first screenshot
    if (mainImage) {
        mediaItems.push({
            type: 'image',
            src: mainImage.src,
            element: mainImage
        });
    }

    updateCounter();
}

// Load thumbnails into carousel
function loadThumbnails() {
    const carousel = document.querySelector('.thumbnail-carousel');
    if (!carousel) return;

    carousel.innerHTML = '';

    mediaItems.forEach((item, index) => {
        const thumb = document.createElement('div');
        thumb.style.position = 'relative';
        thumb.style.cursor = 'pointer';

        const img = document.createElement('img');
        img.src = item.type === 'video' ? mediaItems.find(m => m.type === 'image')?.src : item.src;
        img.className = 'carousel-thumb' + (index === 0 ? ' active' : '');
        img.onclick = () => selectMedia(index);

        // Add play icon for videos
        if (item.type === 'video') {
            const playIcon = document.createElement('div');
            playIcon.textContent = '▶';
            playIcon.style.cssText = 'position:absolute;top:50%;left:50%;transform:translate(-50%,-50%);color:#fff;font-size:1.5rem;pointer-events:none;text-shadow:0 0 5px rgba(0,0,0,0.8)';
            thumb.appendChild(playIcon);
        }

        thumb.appendChild(img);
        carousel.appendChild(thumb);
    });
}

// Select specific media
function selectMedia(index) {
    if (index < 0 || index >= mediaItems.length) return;

    currentMediaIndex = index;
    const item = mediaItems[index];
    const mainImage = document.getElementById('mainImage');
    const mainVideo = document.getElementById('mainVideo');
    const playBtn = document.getElementById('playBtn');

    // Update thumbnails
    document.querySelectorAll('.carousel-thumb').forEach((thumb, i) => {
        thumb.classList.toggle('active', i === index);
    });

    if (item.type === 'video') {
        mainImage.style.display = 'none';
        mainVideo.style.display = 'block';
        mainVideo.src = item.src;
        mainVideo.play();
        playBtn.textContent = '⏸';
    } else {
        mainVideo.pause();
        mainVideo.style.display = 'none';
        mainImage.style.display = 'block';
        mainImage.src = item.src;
        playBtn.textContent = '▶';
    }

    updateCounter();
}

// Next media
function nextMedia() {
    selectMedia((currentMediaIndex + 1) % mediaItems.length);
}

// Previous media
function prevMedia() {
    selectMedia((currentMediaIndex - 1 + mediaItems.length) % mediaItems.length);
}

// Toggle play/pause
function togglePlay() {
    const mainVideo = document.getElementById('mainVideo');
    const playBtn = document.getElementById('playBtn');
    const currentItem = mediaItems[currentMediaIndex];

    if (currentItem.type === 'video') {
        if (mainVideo.paused) {
            mainVideo.play();
            playBtn.textContent = '⏸';
        } else {
            mainVideo.pause();
            playBtn.textContent = '▶';
        }
    } else {
        // If image, switch to video if available
        const videoIndex = mediaItems.findIndex(m => m.type === 'video');
        if (videoIndex >= 0) {
            selectMedia(videoIndex);
        }
    }
}

// Update counter
function updateCounter() {
    const counter = document.getElementById('counter');
    if (counter) {
        counter.textContent = `${currentMediaIndex + 1} / ${mediaItems.length}`;
    }
}

// Toggle fullscreen
function toggleFullscreen() {
    const container = document.querySelector('.main-media-container');

    if (!document.fullscreenElement) {
        if (container.requestFullscreen) {
            container.requestFullscreen();
        }
    } else {
        if (document.exitFullscreen) {
            document.exitFullscreen();
        }
    }
}

// Keyboard shortcuts
document.addEventListener('keydown', function(e) {
    if (e.key === 'ArrowRight') {
        nextMedia();
    } else if (e.key === 'ArrowLeft') {
        prevMedia();
    } else if (e.key === ' ') {
        e.preventDefault();
        togglePlay();
    } else if (e.key === 'f' || e.key === 'F') {
        toggleFullscreen();
    }
});

// Auto-hide controls on video
const mainVideo = document.getElementById('mainVideo');
if (mainVideo) {
    mainVideo.addEventListener('playing', function() {
        document.querySelector('.media-overlay').style.opacity = '0';
    });

    mainVideo.addEventListener('pause', function() {
        document.querySelector('.media-overlay').style.opacity = '1';
    });

    // Show controls on mouse move
    const container = document.querySelector('.main-media-container');
    if (container) {
        container.addEventListener('mousemove', function() {
            document.querySelector('.media-overlay').style.opacity = '1';
            clearTimeout(this.hideTimer);
            this.hideTimer = setTimeout(() => {
                if (!mainVideo.paused) {
                    document.querySelector('.media-overlay').style.opacity = '0';
                }
            }, 2000);
        });
    }
}

// Share functionality
document.querySelectorAll('.share-btn').forEach(btn => {
    btn.addEventListener('click', function() {
        const url = window.location.href;
        const title = document.querySelector('.game-title').textContent;

        if (this.textContent.includes('Facebook')) {
            window.open(`https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(url)}`, '_blank');
        } else if (this.textContent.includes('Twitter')) {
            window.open(`https://twitter.com/intent/tweet?url=${encodeURIComponent(url)}&text=${encodeURIComponent(title)}`, '_blank');
        } else if (this.textContent.includes('Reddit')) {
            window.open(`https://www.reddit.com/submit?url=${encodeURIComponent(url)}&title=${encodeURIComponent(title)}`, '_blank');
        }
    });
});

// Smooth scroll for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    });
});

// Add to cart animation
const buyBtn = document.querySelector('.btn-buy');
if (buyBtn) {
    buyBtn.addEventListener('click', function(e) {
        this.textContent = 'Adding...';
        this.style.background = '#2a475e';
    });
}

// Achievement hover effects
document.querySelectorAll('.achievement-item').forEach(item => {
    item.addEventListener('mouseenter', function() {
        this.style.transform = 'scale(1.05)';
        this.style.transition = 'transform 0.2s';
    });

    item.addEventListener('mouseleave', function() {
        this.style.transform = 'scale(1)';
    });
});

console.log('Game detail page loaded successfully');
console.log(`Media items: ${mediaItems.length}`);
