import { checkAuth } from './auth.js';

document.addEventListener('DOMContentLoaded', async () => {
    try {
        const user = await checkAuth();
        if (!user) return;

        updateUI(user);
        setupEventListeners();

    } catch (error) {
        console.error('Dashboard error:', error);
        window.location.href = '/login.html';
    }
});

function updateUI(user) {
    // Navigation
    document.getElementById('userName').textContent = `${user.firstName} ${user.lastName}`;
    document.getElementById('userInitials').textContent =
        `${user.firstName.charAt(0)}${user.lastName.charAt(0)}`.toUpperCase();
    document.getElementById('userRole').textContent = getRoleName(user.role);

    // User info section
    document.getElementById('sessionFullName').textContent = `${user.firstName} ${user.lastName}`;
    document.getElementById('sessionEmail').textContent = user.email || 'Non renseigné';
    document.getElementById('sessionMatricule').textContent = user.matricule || 'Non renseigné';
    document.getElementById('sessionDepartement').textContent =
        user.departement?.nomDepartement || 'Non renseigné';
    document.getElementById('sessionRole').textContent = getRoleName(user.role);
    document.getElementById('sessionDate').textContent = new Date().toLocaleString('fr-FR');
}

function getRoleName(role) {
    const roles = {
        'ADMIN': 'Administrateur',
        'USER': 'Utilisateur'
    };
    return roles[role] || role;
}

function setupEventListeners() {
    // Logout button
    document.getElementById('logoutBtn').addEventListener('click', async () => {
        await ApiService.logout();
        window.location.href = '/login.html';
    });

    // User menu toggle
    const userMenuButton = document.getElementById('userMenuButton');
    const userMenu = document.getElementById('userMenu');

    userMenuButton.addEventListener('click', () => {
        userMenu.classList.toggle('hidden');
    });

    // Close menu when clicking outside
    document.addEventListener('click', (e) => {
        if (!userMenu.contains(e.target) && !userMenuButton.contains(e.target)) {
            userMenu.classList.add('hidden');
        }
    });
}