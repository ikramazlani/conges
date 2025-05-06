const API_BASE_URL = 'http://localhost:8080';

class ApiService {
    static async login(username, password) {
        const response = await fetch(`${API_BASE_URL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password
            }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Échec de la connexion');
        }

        return await response.json();
    }

    static async logout() {
        const token = localStorage.getItem('token');
        if (!token) return;

        await fetch(`${API_BASE_URL}/logout`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });
    }

    static async getCurrentUser() {
        const token = localStorage.getItem('token');
        if (!token) {
            throw new Error('Aucun token trouvé');
        }

        const response = await fetch(`${API_BASE_URL}/api/users/me`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des informations utilisateur');
        }

        return await response.json();
    }

    static async refreshToken() {
        const refreshToken = localStorage.getItem('refresh_token');
        if (!refreshToken) {
            throw new Error('Aucun refresh token trouvé');
        }

        const response = await fetch(`${API_BASE_URL}/refresh_token`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${refreshToken}`
            }
        });

        if (!response.ok) {
            throw new Error('Échec du rafraîchissement du token');
        }

        const data = await response.json();
        localStorage.setItem('token', data.access_token);
        return data;
    }

    static async fetchWithAuth(url, options = {}) {
        if (!options.headers) {
            options.headers = {};
        }

        const token = localStorage.getItem('token');
        if (token) {
            options.headers['Authorization'] = `Bearer ${token}`;
        }

        let response = await fetch(url, options);

        // Si le token a expiré, on essaie de le rafraîchir
        if (response.status === 401) {
            try {
                await this.refreshToken();
                // On relance la requête avec le nouveau token
                if (token) {
                    options.headers['Authorization'] = `Bearer ${localStorage.getItem('token')}`;
                }
                response = await fetch(url, options);
            } catch (error) {
                // Si le rafraîchissement échoue, on déconnecte
                localStorage.removeItem('token');
                localStorage.removeItem('refresh_token');
                window.location.href = 'index.html';
                throw error;
            }
        }

        return response;
    }


    static async createDemandeConge(demandeData) {
        const token = localStorage.getItem('token');
        if (!token) {
            throw new Error('Utilisateur non authentifié');
        }

        try {
            const response = await fetch(`${API_BASE_URL}/api/demandes-conge`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(demandeData)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Erreur lors de la création de la demande');
            }

            return response;
        } catch (error) {
            console.error('Erreur API:', error);
            throw error;
        }
    }
}