document.addEventListener('DOMContentLoaded', function() {
    // Récupérer les jours fériés depuis le calendrier admin
    const nationalHolidays = {
        '01-01': "Nouvel An",
        '01-11': "Manifeste de l'Indépendance",
        '05-01': "Fête du Travail",
        '07-30': "Fête du Trône",
        '08-14': "Allégeance Oued Eddahab",
        '08-20': "Révolution du Roi et du Peuple",
        '08-21': "Fête de la Jeunesse",
        '11-06': "Marche Verte",
        '11-18': "Fête de l'Indépendance"
    };

    // Récupérer les jours fériés ajoutés par l'admin depuis le localStorage
    let userAddedHolidays = JSON.parse(localStorage.getItem('userAddedHolidays')) || {};

    // Convertir en format compatible avec flatpickr
    function getDisabledDates(year) {
        const disabledDates = [];

        // Ajouter les jours fériés nationaux
        for (const [date, name] of Object.entries(nationalHolidays)) {
            disabledDates.push(`${year}-${date}`);
        }

        // Ajouter les jours fériés religieux ajoutés par l'admin
        for (const [date, holiday] of Object.entries(userAddedHolidays)) {
            const dateObj = new Date(date);
            if (dateObj.getFullYear() === year) {
                disabledDates.push(date);
            }
        }

        return disabledDates;
    }

    // Configuration du date picker
    const currentYear = new Date().getFullYear();
    const disabledDates = getDisabledDates(currentYear);

    const datePickerOptions = {
        locale: 'fr',
        dateFormat: 'd/m/Y',
        disable: [
            function(date) {
                // Désactiver les weekends (samedi=6, dimanche=0)
                return (date.getDay() === 0 || date.getDay() === 6);
            },
            // Désactiver les jours fériés
            ...disabledDates
        ]
    };

    const startDatePicker = flatpickr('#startDate', {
        ...datePickerOptions,
        minDate: 'today',
        onChange: function(selectedDates, dateStr, instance) {
            if (selectedDates.length > 0) {
                endDatePicker.set('minDate', selectedDates[0]);
                calculateDuration();
            }
        }
    });

    const endDatePicker = flatpickr('#endDate', {
        ...datePickerOptions,
        minDate: 'today',
        onChange: function(selectedDates, dateStr, instance) {
            calculateDuration();
        }
    });

    // Fonction pour vérifier si une date est un jour férié
    function isHoliday(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const dateKey = `${month}-${day}`;
        const fullDateKey = `${year}-${month}-${day}`;

        // Vérifier les jours fériés nationaux
        if (nationalHolidays[dateKey]) {
            return true;
        }

        // Vérifier les jours fériés ajoutés par l'admin
        if (userAddedHolidays[fullDateKey]) {
            return true;
        }

        return false;
    }

    // Calcul de la durée
    function calculateDuration() {
        const startDate = startDatePicker.selectedDates[0];
        const endDate = endDatePicker.selectedDates[0];

        if (!startDate || !endDate) return;

        // Vérifier que la date de fin est après la date de début
        if (endDate < startDate) {
            endDatePicker.clear();
            showError('La date de fin doit être après la date de début');
            return;
        }

        // Calculer la durée en jours ouvrés
        let duration = 0;
        let currentDate = new Date(startDate);

        while (currentDate <= endDate) {
            const dayOfWeek = currentDate.getDay();

            // Ne pas compter les weekends et jours fériés
            if (dayOfWeek !== 0 && dayOfWeek !== 6 && !isHoliday(currentDate)) {
                duration++;
            }

            currentDate.setDate(currentDate.getDate() + 1);
        }

        // Vérifier la durée maximale (22 jours)
        if (duration > 22) {
            showError('La durée maximale de congé est de 22 jours');
            endDatePicker.clear();
            document.getElementById('durationInfo').classList.add('hidden');
            return;
        }

        document.getElementById('durationDays').textContent = duration;
        document.getElementById('durationInfo').classList.remove('hidden');
    }

    // Gestion du formulaire
    document.getElementById('leaveForm').addEventListener('submit', async function(e) {
        e.preventDefault();

        const submitBtn = document.getElementById('submitBtn');
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i> Envoi en cours...';

        try {
            // Simuler l'envoi au serveur
            await new Promise(resolve => setTimeout(resolve, 1500));

            showToast('Demande envoyée avec succès!');
            setTimeout(() => {
                window.location.href = 'dashboard.html';
            }, 2000);
        } catch (error) {
            showError(error.message);
            submitBtn.disabled = false;
            submitBtn.innerHTML = '<i class="fas fa-paper-plane mr-2"></i> Soumettre';
        }
    });

    // Bouton annuler
    document.getElementById('cancelBtn').addEventListener('click', function() {
        window.location.href = 'dashboard.html';
    });

    // Déconnexion
    document.getElementById('logoutBtn').addEventListener('click', function() {
        localStorage.removeItem('token');
        localStorage.removeItem('refresh_token');
        window.location.href = '../index.html';
    });

    // Fonctions d'affichage des messages
    function showToast(message) {
        const toast = document.getElementById('toast');
        document.getElementById('toastMessage').textContent = message;
        toast.classList.remove('hidden');
        setTimeout(() => toast.classList.add('hidden'), 3000);
    }

    function showError(message) {
        const toast = document.getElementById('toast');
        toast.querySelector('#toastMessage').textContent = message;
        toast.classList.remove('hidden', 'bg-green-500');
        toast.classList.add('bg-red-500');
        setTimeout(() => {
            toast.classList.add('hidden');
            toast.classList.remove('bg-red-500');
            toast.classList.add('bg-green-500');
        }, 3000);
    }
});