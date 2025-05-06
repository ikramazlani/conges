// Vérifie si l'utilisateur est authentifié
function isAuthenticated() {
    return !!localStorage.getItem('token');
}

// Redirige vers la page de login si non authentifié
function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = 'js/html/index.html';
    }
}

// Intercepte les requêtes pour ajouter le token et gérer les erreurs 401
async function fetchWithAuth(url, options = {}) {
    if (!options.headers) {
        options.headers = {};
    }

    const token = localStorage.getItem('token');
    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(url, options);

    // Si le token a expiré, on essaie de le rafraîchir
    if (response.status === 401) {
        try {
            await ApiService.refreshToken();
            // On relance la requête avec le nouveau token
            return fetchWithAuth(url, options);
        } catch (error) {
            // Si le rafraîchissement échoue, on déconnecte
            localStorage.removeItem('token');
            localStorage.removeItem('refresh_token');
            window.location.href = '../html/index.html';
            throw error;
        }
    }

    return response;
}