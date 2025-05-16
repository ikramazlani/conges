// src/main/resources/static/js/calendarManager.js
class CalendarManager {
    constructor() {
        this.currentYear = new Date().getFullYear();
        this.months = [
            'Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin',
            'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'
        ];
        this.days = ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'];
        this.holidays = [];

        this.init();
    }

    async init() {
        await this.loadHolidays();
        this.renderYearSelector();
        this.renderCalendar();
        this.setupEventListeners();
    }

    async loadHolidays() {
        try {
            this.holidays = await ReligiousHolidaysAPI.getByYear(this.currentYear);
        } catch (error) {
            console.error('Erreur lors du chargement des fêtes:', error);
        }
    }

    renderYearSelector() {
        const yearSelector = document.querySelector('.year-selector');
        if (!yearSelector) return;

        yearSelector.innerHTML = `
            <button id="prevYear" class="p-2 rounded hover:bg-gray-100">
                <i class="fas fa-chevron-left"></i>
            </button>
            <span class="current-year mx-4 text-xl font-semibold">${this.currentYear}</span>
            <button id="nextYear" class="p-2 rounded hover:bg-gray-100">
                <i class="fas fa-chevron-right"></i>
            </button>
        `;
    }

    renderCalendar() {
        const calendarContainer = document.querySelector('.months-container');
        if (!calendarContainer) return;

        calendarContainer.innerHTML = this.months
            .map((month, index) => this.renderMonth(month, index))
            .join('');
    }

    renderMonth(monthName, monthIndex) {
        const firstDay = new Date(this.currentYear, monthIndex, 1).getDay();
        const daysInMonth = new Date(this.currentYear, monthIndex + 1, 0).getDate();
        const monthHolidays = this.getHolidaysForMonth(monthIndex);

        let days = '';
        // Jours vides avant le premier jour du mois
        for (let i = 0; i < firstDay; i++) {
            days += '<div class="day other-month"></div>';
        }

        // Jours du mois
        for (let day = 1; day <= daysInMonth; day++) {
            const date = new Date(this.currentYear, monthIndex, day);
            const isWeekend = date.getDay() === 0 || date.getDay() === 6;
            const isToday = this.isToday(day, monthIndex);
            const holiday = this.getHolidayForDate(day, monthIndex);

            let dayClasses = 'day';
            if (isWeekend) dayClasses += ' weekend';
            if (isToday) dayClasses += ' today';
            if (holiday) dayClasses += ' holiday-type-2';

            days += `
                <div class="${dayClasses}"
                     data-day="${day}"
                     data-month="${monthIndex}"
                     title="${holiday ? holiday.name : ''}">
                    ${day}
                </div>
            `;
        }

        return `
            <div class="month-container">
                <div class="month-header">
                    <h3 class="month-name">${monthName} ${this.currentYear}</h3>
                </div>
                <div class="days-header">
                    ${this.days.map(day => `<div>${day}</div>`).join('')}
                </div>
                <div class="days-grid">
                    ${days}
                </div>
                ${this.renderHolidaysList(monthHolidays)}
            </div>
        `;
    }

    renderHolidaysList(holidays) {
        if (!holidays.length) return '';

        return `
            <div class="mt-4">
                <h4 class="text-sm font-semibold mb-2">Jours fériés :</h4>
                <div class="holidays-list">
                    ${holidays.map(holiday => `
                        <div class="holiday-item flex justify-between items-center p-2 hover:bg-gray-50 rounded"
                             data-holiday-id="${holiday.id}">
                            <span>${holiday.name} (${new Date(holiday.date).getDate()}/${new Date(holiday.date).getMonth() + 1})</span>
                            <button class="delete-holiday text-red-500 hover:text-red-700">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                    `).join('')}
                </div>
            </div>
        `;
    }

    setupEventListeners() {
        // Navigation entre les années
        document.getElementById('prevYear')?.addEventListener('click', () => this.changeYear(-1));
        document.getElementById('nextYear')?.addEventListener('click', () => this.changeYear(1));

        // Ajout d'une fête
        document.getElementById('addHolidayBtn')?.addEventListener('click', () => {
            this.showAddHolidayModal();
        });

        // Soumission du formulaire
        document.getElementById('holidayForm')?.addEventListener('submit', (e) => this.handleSubmitHoliday(e));

        // Suppression d'une fête
        document.addEventListener('click', (e) => {
            if (e.target.closest('.delete-holiday')) {
                const holidayItem = e.target.closest('.holiday-item');
                const holidayId = holidayItem?.dataset.holidayId;
                if (holidayId) this.handleDeleteHoliday(holidayId);
            }
        });
    }

    async changeYear(offset) {
        this.currentYear += offset;
        document.querySelector('.current-year').textContent = this.currentYear;
        await this.loadHolidays();
        this.renderCalendar();
    }

    showAddHolidayModal() {
        const modal = document.getElementById('addHolidayModal');
        if (modal) modal.style.display = 'block';
    }

    async handleSubmitHoliday(e) {
        e.preventDefault();
        const form = e.target;

        const holidayData = {
            name: form.holidayName.value,
            date: form.holidayDate.value,
            duration: parseInt(form.holidayDuration.value) || 1,
            isRecurring: form.holidayRecurring.checked
        };

        try {
            await ReligiousHolidaysAPI.add(holidayData);
            await this.loadHolidays();
            this.renderCalendar();
            form.reset();
            document.getElementById('addHolidayModal').style.display = 'none';
        } catch (error) {
            alert('Erreur lors de l\'ajout de la fête: ' + error.message);
        }
    }

    async handleDeleteHoliday(holidayId) {
        if (!confirm('Êtes-vous sûr de vouloir supprimer cette fête ?')) return;

        try {
            await ReligiousHolidaysAPI.delete(holidayId);
            this.holidays = this.holidays.filter(h => h.id !== holidayId);
            this.renderCalendar();
        } catch (error) {
            alert('Erreur lors de la suppression: ' + error.message);
        }
    }

    // Méthodes utilitaires
    getHolidaysForMonth(monthIndex) {
        return this.holidays.filter(holiday => {
            const holidayDate = new Date(holiday.date);
            return holidayDate.getMonth() === monthIndex &&
                   holidayDate.getFullYear() === this.currentYear;
        });
    }

    getHolidayForDate(day, monthIndex) {
        return this.holidays.find(holiday => {
            const holidayDate = new Date(holiday.date);
            return holidayDate.getDate() === day &&
                   holidayDate.getMonth() === monthIndex &&
                   holidayDate.getFullYear() === this.currentYear;
        });
    }

    isToday(day, monthIndex) {
        const today = new Date();
        return day === today.getDate() &&
               monthIndex === today.getMonth() &&
               this.currentYear === today.getFullYear();
    }
}

// Initialisation
document.addEventListener('DOMContentLoaded', () => {
    window.calendarManager = new CalendarManager();
});