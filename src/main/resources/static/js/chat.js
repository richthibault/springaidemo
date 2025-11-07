// Chat functionality
let isProcessing = false;

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    // Allow Enter key to send message (Shift+Enter for new line)
    document.getElementById('messageInput').addEventListener('keydown', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });

    // Add welcome message
    addBotMessage("Hello! I'm an AI assistant powered by Spring AI. How can I help you today?");
});

// Send message function
async function sendMessage() {
    if (isProcessing) {
        return;
    }

    const messageInput = document.getElementById('messageInput');
    const message = messageInput.value.trim();

    if (!message) {
        return;
    }

    // Get selected chat mode
    const chatMode = document.querySelector('input[name="chatMode"]:checked').value;
    const endpoint = chatMode === 'rag' ? '/api/chat/rag' : '/api/chat';

    // Add user message to chat
    addUserMessage(message);

    // Clear input
    messageInput.value = '';
    messageInput.style.height = 'auto';

    // Disable send button and show typing indicator
    isProcessing = true;
    updateSendButton();
    showTypingIndicator();

    try {
        // Send request to backend
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ message: message })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        
        // Remove typing indicator and add bot response
        removeTypingIndicator();
        addBotMessage(data.response);
        
        updateStatus('');
    } catch (error) {
        console.error('Error:', error);
        removeTypingIndicator();
        addBotMessage('Sorry, I encountered an error. Please try again later.');
        updateStatus('Error: ' + error.message);
    } finally {
        isProcessing = false;
        updateSendButton();
    }
}

// Add user message to chat
function addUserMessage(message) {
    const messagesContainer = document.getElementById('messages');
    const messageDiv = document.createElement('div');
    messageDiv.className = 'message user-message';
    messageDiv.textContent = message;
    messagesContainer.appendChild(messageDiv);
    scrollToBottom();
}

// Add bot message to chat
function addBotMessage(message) {
    const messagesContainer = document.getElementById('messages');
    const messageDiv = document.createElement('div');
    messageDiv.className = 'message bot-message';
    messageDiv.textContent = message;
    messagesContainer.appendChild(messageDiv);
    scrollToBottom();
}

// Show typing indicator
function showTypingIndicator() {
    const messagesContainer = document.getElementById('messages');
    const typingDiv = document.createElement('div');
    typingDiv.className = 'typing-indicator';
    typingDiv.id = 'typing-indicator';
    typingDiv.innerHTML = '<span></span><span></span><span></span>';
    messagesContainer.appendChild(typingDiv);
    scrollToBottom();
}

// Remove typing indicator
function removeTypingIndicator() {
    const typingIndicator = document.getElementById('typing-indicator');
    if (typingIndicator) {
        typingIndicator.remove();
    }
}

// Update status message
function updateStatus(message) {
    const statusDiv = document.getElementById('status');
    statusDiv.textContent = message;
}

// Update send button state
function updateSendButton() {
    const sendButton = document.getElementById('sendButton');
    sendButton.disabled = isProcessing;
    sendButton.textContent = isProcessing ? 'Sending...' : 'Send';
}

// Scroll to bottom of chat
function scrollToBottom() {
    const chatContainer = document.getElementById('chat-container');
    chatContainer.scrollTop = chatContainer.scrollHeight;
}

// Auto-resize textarea
document.getElementById('messageInput').addEventListener('input', function() {
    this.style.height = 'auto';
    this.style.height = Math.min(this.scrollHeight, 150) + 'px';
});
