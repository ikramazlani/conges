// src/main/resources/static/js/api/religiousHolidays.js
class ReligiousHolidaysAPI {
    static BASE_URL = '/api/holidays/religious';

    static async getAll() {
        try {
            const response = await fetch(this.BASE_URL);
            if (!response.ok) throw new Error('Erreur lors de la récupération des fêtes');
            return await response.json();
        } catch (error) {
            console.error('Erreur API:', error);
            return [];
        }
    }

    static async getByYear(year) {
        try {
            const response = await fetch(`${this.BASE_URL}/year/${year}`);
            if (!response.ok) throw new Error('Erreur lors de la récupération des fêtes');
            return await response.json();
        } catch (error) {
            console.error('Erreur API:', error);
            return [];
        }
    }

    static async add(holiday) {
        try {
            const response = await fetch(this.BASE_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(holiday)
            });
            if (!response.ok) throw new Error('Erreur lors de l\'ajout');
            return await response.json();
        } catch (error) {
            console.error('Erreur API:', error);
            throw error;
        }
    }

    static async delete(id) {
        try {
            const response = await fetch(`${this.BASE_URL}/${id}`, {
                method: 'DELETE'
            });
            if (!response.ok) throw new Error('Erreur lors de la suppression');
        } catch (error) {
            console.error('Erreur API:', error);
            throw error;
        }
    }
}