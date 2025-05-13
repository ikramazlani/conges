// Fonction principale pour initialiser le calendrier
document.addEventListener('DOMContentLoaded', function() {
    // Initialiser avec l'année courante
    const currentDate = new Date();
    let currentYear = currentDate.getFullYear();
    
    // Jours fériés fixes du Maroc avec types/couleurs
    const fixedHolidays = {
        '1-1': { 
            name: "Nouvel An", 
            type: 1 
        },
        '1-11': { 
            name: "Manifeste de l'Indépendance", 
            type: 1 
        },
        '5-1': { 
            name: "Fête du Travail", 
            type: 1 
        },
        '7-30': { 
            name: "Fête du Trône", 
            type: 1 
        },
        '8-14': { 
            name: "Allégeance Oued Eddahab", 
            type: 1 
        },
        '8-20': { 
            name: "Révolution du Roi et du Peuple", 
            type: 1 
        },
        '8-21': { 
            name: "Fête de la Jeunesse", 
            type: 1 
        },
        '11-6': { 
            name: "Marche Verte", 
            type: 1 
        },
        '11-18': { 
            name: "Fête de l'Indépendance", 
            type: 1 
        }
    };
    
    // Calcul des jours fériés religieux (approximatif)
    function getIslamicHolidays(year) {
        // Ces dates sont approximatives car le calendrier islamique est lunaire
        // En pratique, elles sont déterminées par l'observation de la lune
        
        // Calcul approximatif de Aïd al-Fitr (fin du Ramadan)
        let ramadanEnd = new Date(year, 3, 1); // Approx. début avril
        ramadanEnd.setDate(ramadanEnd.getDate() + 29); // Ramadan dure 29 ou 30 jours
        
        // Calcul approximatif de Aïd al-Adha (10 Dhou al-Hijja)
        let aidAlAdha = new Date(year, 6, 1); // Approx. début juillet
        aidAlAdha.setDate(aidAlAdha.getDate() + 9); // 10 Dhou al-Hijja
        
        // Nouvel An islamique (1 Muharram)
        let islamicNewYear = new Date(year, 8, 1); // Approx. début septembre
        
        // Anniversaire du Prophète (12 Rabi' al-Awwal)
        let mawlid = new Date(year, 10, 1); // Approx. début novembre
        
        return {
            [ramadanEnd.getMonth()+1 + '-' + ramadanEnd.getDate()]: { 
                name: "Aïd al-Fitr", 
                type: 2 
            },
            [aidAlAdha.getMonth()+1 + '-' + aidAlAdha.getDate()]: { 
                name: "Aïd al-Adha", 
                type: 2 
            },
            [islamicNewYear.getMonth()+1 + '-' + islamicNewYear.getDate()]: { 
                name: "Nouvel An islamique", 
                type: 2 
            },
            [mawlid.getMonth()+1 + '-' + mawlid.getDate()]: { 
                name: "Mawlid", 
                type: 2 
            }
        };
    }
    
    // Mettre à jour l'affichage du calendrier
    function updateCalendar() {
        document.getElementById('current-year').textContent = currentYear;
        renderMonths(currentYear);
    }
    
    // Rendre tous les mois pour une année donnée
    function renderMonths(year) {
        const monthsContainer = document.getElementById('months-container');
        monthsContainer.innerHTML = '';
        
        // Obtenir les jours fériés pour cette année
        const holidays = {...fixedHolidays, ...getIslamicHolidays(year)};
        
        for (let month = 0; month < 12; month++) {
            const monthElement = createMonthElement(year, month, holidays);
            monthsContainer.appendChild(monthElement);
        }
    }
    
    // Créer un élément HTML pour un mois spécifique
    function createMonthElement(year, month, holidays) {
        const monthNames = [
            'Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin',
            'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'
        ];
        
        const firstDay = new Date(year, month, 1);
        const daysInMonth = new Date(year, month + 1, 0).getDate();
        const startingDay = firstDay.getDay(); // 0 (dimanche) à 6 (samedi)
        
        // Créer le conteneur du mois
        const monthContainer = document.createElement('div');
        monthContainer.className = 'month-container';
        
        // Créer l'en-tête du mois avec le nom et les jours fériés
        const monthHeader = document.createElement('div');
        monthHeader.className = 'month-header';
        
        const monthNameElement = document.createElement('div');
        monthNameElement.className = 'month-name';
        monthNameElement.textContent = monthNames[month];
        monthHeader.appendChild(monthNameElement);
        
        monthContainer.appendChild(monthHeader);
        
        // Ajouter la liste des jours fériés de ce mois
        const monthHolidaysElement = document.createElement('div');
        monthHolidaysElement.className = 'month-holidays';
        
        // Trouver les jours fériés de ce mois
        const monthHolidays = {};
        const monthKey = (month + 1) + '-';
        
        for (const [date, holiday] of Object.entries(holidays)) {
            if (date.startsWith(monthKey)) {
                monthHolidays[date] = holiday;
            }
        }
        
        // Afficher les jours fériés de ce mois
        for (const [date, holiday] of Object.entries(monthHolidays)) {
            const day = date.split('-')[1];
            const holidayItem = document.createElement('div');
            holidayItem.className = 'holiday-item';
            
            const holidayColor = document.createElement('div');
            holidayColor.className = 'holiday-color';
            holidayColor.style.backgroundColor = getHolidayColor(holiday.type);
            holidayColor.style.borderColor = getHolidayBorderColor(holiday.type);
            
            const holidayText = document.createElement('span');
            holidayText.textContent = `${day} ${holiday.name}`;
            
            holidayItem.appendChild(holidayColor);
            holidayItem.appendChild(holidayText);
            monthHolidaysElement.appendChild(holidayItem);
        }
        
        monthContainer.appendChild(monthHolidaysElement);
        
        // Ajouter les en-têtes des jours
        const daysHeader = document.createElement('div');
        daysHeader.className = 'days-header';
        
        const dayNames = ['Di', 'Lu', 'Ma', 'Me', 'Je', 'Ve', 'Sa'];
        dayNames.forEach(day => {
            const dayElement = document.createElement('div');
            dayElement.textContent = day;
            daysHeader.appendChild(dayElement);
        });
        
        monthContainer.appendChild(daysHeader);
        
        // Ajouter la grille des jours
        const daysGrid = document.createElement('div');
        daysGrid.className = 'days-grid';
        
        // Ajouter les cases vides pour les jours du mois précédent
        for (let i = 0; i < startingDay; i++) {
            const emptyDay = document.createElement('div');
            emptyDay.className = 'day other-month';
            daysGrid.appendChild(emptyDay);
        }
        
        // Ajouter les jours du mois
        const today = new Date();
        const isCurrentMonthAndYear = today.getFullYear() === year && today.getMonth() === month;
        
        for (let day = 1; day <= daysInMonth; day++) {
            const dayElement = document.createElement('div');
            dayElement.className = 'day';
            dayElement.textContent = day;
            
            // Vérifier si c'est un week-end
            const dayOfWeek = new Date(year, month, day).getDay();
            if (dayOfWeek === 0 || dayOfWeek === 6) {
                dayElement.classList.add('weekend');
            }
            
            // Vérifier si c'est aujourd'hui
            if (isCurrentMonthAndYear && day === today.getDate()) {
                dayElement.classList.add('today');
            }
            
            // Vérifier si c'est un jour férié
            const holidayKey = (month+1) + '-' + day;
            if (holidays[holidayKey]) {
                dayElement.classList.add(`holiday-type-${holidays[holidayKey].type}`);
            }
            
            daysGrid.appendChild(dayElement);
        }
        
        monthContainer.appendChild(daysGrid);
        return monthContainer;
    }
    
    // Obtenir la couleur en fonction du type de jour férié
    function getHolidayColor(type) {
        switch(type) {
            case 1: return '#ffcdd2'; // Fêtes nationales
            case 2: return '#c8e6c9'; // Fêtes religieuses
            default: return '#bbdefb';
        }
    }
    
    // Obtenir la couleur de bordure en fonction du type de jour férié
    function getHolidayBorderColor(type) {
        switch(type) {
            case 1: return '#ef9a9a';
            case 2: return '#a5d6a7';
            default: return '#90caf9';
        }
    }
    
    // Gestionnaires d'événements pour les boutons de navigation
    document.getElementById('prev-year').addEventListener('click', function() {
        currentYear--;
        updateCalendar();
    });
    
    document.getElementById('next-year').addEventListener('click', function() {
        currentYear++;
        updateCalendar();
    });
    
    // Initialiser le calendrier
    updateCalendar();
    
    // Fonction pour permettre le changement d'année via le clavier
    document.addEventListener('keydown', function(e) {
        if (e.key === 'ArrowLeft') {
            currentYear--;
            updateCalendar();
        } else if (e.key === 'ArrowRight') {
            currentYear++;
            updateCalendar();
        }
    });
});