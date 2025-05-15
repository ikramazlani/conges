document.addEventListener('DOMContentLoaded', function() {
  const currentYearElement = document.getElementById('current-year');
  const monthsContainer = document.getElementById('months-container');
  const prevYearButton = document.getElementById('prev-year');
  const nextYearButton = document.getElementById('next-year');
  
  let currentYear = new Date().getFullYear();
  
  // Jours fériés nationaux fixes
  const nationalHolidays = {
      '01-01': {name: "Nouvel An", type: 1},
      '01-11': {name: "Manifeste de l'Indépendance", type: 1},
      '05-01': {name: "Fête du Travail", type: 1},
      '07-30': {name: "Fête du Trône", type: 1},
      '08-14': {name: "Allégeance Oued Eddahab", type: 1},
      '08-20': {name: "Révolution du Roi et du Peuple", type: 1},
      '08-21': {name: "Fête de la Jeunesse", type: 1},
      '11-06': {name: "Marche Verte", type: 1},
      '11-18': {name: "Fête de l'Indépendance", type: 1}
  };

  // Jours fériés religieux précalculés (2000-2024)
  const religiousHolidays = {
      // Achoura (10 Muharram)
      '2000-04-16': {name: "Achoura", type: 2},
      '2001-04-05': {name: "Achoura", type: 2},
      '2002-03-25': {name: "Achoura", type: 2},
      '2003-03-14': {name: "Achoura", type: 2},
      '2004-03-02': {name: "Achoura", type: 2},
      '2005-02-19': {name: "Achoura", type: 2},
      '2006-02-09': {name: "Achoura", type: 2},
      '2007-01-29': {name: "Achoura", type: 2},
      '2008-01-19': {name: "Achoura", type: 2},
      '2009-01-07': {name: "Achoura", type: 2},
      '2010-12-27': {name: "Achoura", type: 2},
      '2011-12-06': {name: "Achoura", type: 2},
      '2012-11-24': {name: "Achoura", type: 2},
      '2013-11-14': {name: "Achoura", type: 2},
      '2014-11-03': {name: "Achoura", type: 2},
      '2015-10-24': {name: "Achoura", type: 2},
      '2016-10-12': {name: "Achoura", type: 2},
      '2017-10-01': {name: "Achoura", type: 2},
      '2018-09-21': {name: "Achoura", type: 2},
      '2019-09-10': {name: "Achoura", type: 2},
      '2020-08-29': {name: "Achoura", type: 2},
      '2021-08-19': {name: "Achoura", type: 2},
      '2022-08-08': {name: "Achoura", type: 2},
      '2023-07-28': {name: "Achoura", type: 2},
      '2024-07-16': {name: "Achoura", type: 2},

      // Aïd al-Fitr (1 Shawwal)
      '2000-01-08': {name: "Aïd al-Fitr", type: 2},
      '2001-12-27': {name: "Aïd al-Fitr", type: 2},
      '2002-12-06': {name: "Aïd al-Fitr", type: 2},
      '2003-11-25': {name: "Aïd al-Fitr", type: 2},
      '2004-11-14': {name: "Aïd al-Fitr", type: 2},
      '2005-11-03': {name: "Aïd al-Fitr", type: 2},
      '2006-10-24': {name: "Aïd al-Fitr", type: 2},
      '2007-10-13': {name: "Aïd al-Fitr", type: 2},
      '2008-10-01': {name: "Aïd al-Fitr", type: 2},
      '2009-09-20': {name: "Aïd al-Fitr", type: 2},
      '2010-09-10': {name: "Aïd al-Fitr", type: 2},
      '2011-08-30': {name: "Aïd al-Fitr", type: 2},
      '2012-08-19': {name: "Aïd al-Fitr", type: 2},
      '2013-08-08': {name: "Aïd al-Fitr", type: 2},
      '2014-07-28': {name: "Aïd al-Fitr", type: 2},
      '2015-07-17': {name: "Aïd al-Fitr", type: 2},
      '2016-07-06': {name: "Aïd al-Fitr", type: 2},
      '2017-06-25': {name: "Aïd al-Fitr", type: 2},
      '2018-06-15': {name: "Aïd al-Fitr", type: 2},
      '2019-06-04': {name: "Aïd al-Fitr", type: 2},
      '2020-05-24': {name: "Aïd al-Fitr", type: 2},
      '2021-05-13': {name: "Aïd al-Fitr", type: 2},
      '2022-05-02': {name: "Aïd al-Fitr", type: 2},
      '2023-04-21': {name: "Aïd al-Fitr", type: 2},
      '2024-04-10': {name: "Aïd al-Fitr", type: 2},

      // Aïd al-Adha (10 Dhu al-Hijjah)
      '2000-03-16': {name: "Aïd al-Adha", type: 2},
      '2001-03-05': {name: "Aïd al-Adha", type: 2},
      '2002-02-22': {name: "Aïd al-Adha", type: 2},
      '2003-02-11': {name: "Aïd al-Adha", type: 2},
      '2004-02-01': {name: "Aïd al-Adha", type: 2},
      '2005-01-21': {name: "Aïd al-Adha", type: 2},
      '2006-01-10': {name: "Aïd al-Adha", type: 2},
      '2006-12-31': {name: "Aïd al-Adha", type: 2},
      '2007-12-20': {name: "Aïd al-Adha", type: 2},
      '2008-12-08': {name: "Aïd al-Adha", type: 2},
      '2009-11-27': {name: "Aïd al-Adha", type: 2},
      '2010-11-16': {name: "Aïd al-Adha", type: 2},
      '2011-11-06': {name: "Aïd al-Adha", type: 2},
      '2012-10-26': {name: "Aïd al-Adha", type: 2},
      '2013-10-15': {name: "Aïd al-Adha", type: 2},
      '2014-10-04': {name: "Aïd al-Adha", type: 2},
      '2015-09-23': {name: "Aïd al-Adha", type: 2},
      '2016-09-12': {name: "Aïd al-Adha", type: 2},
      '2017-09-01': {name: "Aïd al-Adha", type: 2},
      '2018-08-21': {name: "Aïd al-Adha", type: 2},
      '2019-08-11': {name: "Aïd al-Adha", type: 2},
      '2020-07-31': {name: "Aïd al-Adha", type: 2},
      '2021-07-20': {name: "Aïd al-Adha", type: 2},
      '2022-07-09': {name: "Aïd al-Adha", type: 2},
      '2023-06-28': {name: "Aïd al-Adha", type: 2},
      '2024-06-16': {name: "Aïd al-Adha", type: 2},

      // Nouvel An islamique (1 Muharram)
      '2000-04-06': {name: "Nouvel An islamique", type: 2},
      '2001-03-26': {name: "Nouvel An islamique", type: 2},
      '2002-03-15': {name: "Nouvel An islamique", type: 2},
      '2003-03-04': {name: "Nouvel An islamique", type: 2},
      '2004-02-21': {name: "Nouvel An islamique", type: 2},
      '2005-02-10': {name: "Nouvel An islamique", type: 2},
      '2006-01-31': {name: "Nouvel An islamique", type: 2},
      '2007-01-20': {name: "Nouvel An islamique", type: 2},
      '2008-01-10': {name: "Nouvel An islamique", type: 2},
      '2008-12-29': {name: "Nouvel An islamique", type: 2},
      '2009-12-18': {name: "Nouvel An islamique", type: 2},
      '2010-12-07': {name: "Nouvel An islamique", type: 2},
      '2011-11-26': {name: "Nouvel An islamique", type: 2},
      '2012-11-15': {name: "Nouvel An islamique", type: 2},
      '2013-11-04': {name: "Nouvel An islamique", type: 2},
      '2014-10-25': {name: "Nouvel An islamique", type: 2},
      '2015-10-14': {name: "Nouvel An islamique", type: 2},
      '2016-10-02': {name: "Nouvel An islamique", type: 2},
      '2017-09-21': {name: "Nouvel An islamique", type: 2},
      '2018-09-11': {name: "Nouvel An islamique", type: 2},
      '2019-08-31': {name: "Nouvel An islamique", type: 2},
      '2020-08-20': {name: "Nouvel An islamique", type: 2},
      '2021-08-09': {name: "Nouvel An islamique", type: 2},
      '2022-07-30': {name: "Nouvel An islamique", type: 2},
      '2023-07-19': {name: "Nouvel An islamique", type: 2},
      '2024-07-07': {name: "Nouvel An islamique", type: 2}
  };

  // Stockage des jours fériés ajoutés par l'utilisateur
  let userAddedHolidays = JSON.parse(localStorage.getItem('userAddedHolidays')) || {};

  function updateYear() {
      currentYearElement.textContent = currentYear;
      monthsContainer.innerHTML = '<div class="loading">Chargement du calendrier...</div>';

      // Fusionner les fêtes nationales, religieuses et celles ajoutées par l'utilisateur
      const allHolidays = {...nationalHolidays};

      // Ajouter les fêtes religieuses uniquement pour les années avant 2025
      if (currentYear < 2025) {
          for (const [date, holiday] of Object.entries(religiousHolidays)) {
              const dateParts = date.split('-');
              const holidayYear = parseInt(dateParts[0]);
              const holidayMonth = dateParts[1];
              const holidayDay = dateParts[2];

              if (holidayYear === currentYear) {
                  const key = `${holidayMonth}-${holidayDay}`;
                  allHolidays[key] = holiday;
              }
          }
      }

      // Ajouter les jours fériés ajoutés par l'utilisateur pour l'année courante
      for (const [date, holiday] of Object.entries(userAddedHolidays)) {
          const dateParts = date.split('-');
          const holidayYear = parseInt(dateParts[0]);

          if (holidayYear === currentYear) {
              const holidayMonth = dateParts[1];
              const holidayDay = dateParts[2];
              const key = `${holidayMonth}-${holidayDay}`;
              allHolidays[key] = holiday;
          }
      }

      renderCalendar(allHolidays);
  }
  
  function renderCalendar(holidays) {
      monthsContainer.innerHTML = '';
      
      for (let month = 0; month < 12; month++) {
          const monthElement = document.createElement('div');
          monthElement.className = 'month-container';
          
          const monthName = new Date(currentYear, month, 1).toLocaleString('fr-FR', { month: 'long' });
          const capitalizedMonthName = monthName.charAt(0).toUpperCase() + monthName.slice(1);
          
          // Trouver les jours fériés pour ce mois
          const monthHolidays = [];
          for (const [date, holiday] of Object.entries(holidays)) {
              const [holidayMonth, holidayDay] = date.split('-');
              if (parseInt(holidayMonth) === month + 1) {
                  monthHolidays.push({
                      day: parseInt(holidayDay),
                      name: holiday.name,
                      type: holiday.type,
                      date: `${currentYear}-${holidayMonth}-${holidayDay}`
                  });
              }
          }
          
          // En-tête du mois
          const monthHeader = document.createElement('div');
          monthHeader.className = 'month-header';
          monthHeader.innerHTML = `
              <div class="month-name">${capitalizedMonthName} ${currentYear}</div>
          `;
          
          // Liste des jours fériés
          const holidaysList = document.createElement('div');
          holidaysList.className = 'month-holidays';
          
          if (monthHolidays.length > 0) {
              monthHolidays.forEach(holiday => {
                  const holidayItem = document.createElement('div');
                  holidayItem.className = 'holiday-item';
                  
                  const colorStyle = holiday.type === 1 ? 
                      "background-color: #ffcdd2; border-color: #ef9a9a;" : 
                      "background-color: #c8e6c9; border-color: #a5d6a7;";
                  
                  holidayItem.innerHTML = `
                      <div class="holiday-color" style="${colorStyle}"></div>
                      <span>${holiday.day} ${capitalizedMonthName}: ${holiday.name}</span>
                  `;
                  holidaysList.appendChild(holidayItem);
              });
          } else {
              holidaysList.textContent = "Aucun jour férié ce mois";
          }
          
          // En-tête des jours de la semaine
          const daysHeader = document.createElement('div');
          daysHeader.className = 'days-header';
          daysHeader.innerHTML = `
              <div>Lun</div>
              <div>Mar</div>
              <div>Mer</div>
              <div>Jeu</div>
              <div>Ven</div>
              <div>Sam</div>
              <div>Dim</div>
          `;
          
          // Grille des jours
          const daysGrid = document.createElement('div');
          daysGrid.className = 'days-grid';
          
          const firstDay = new Date(currentYear, month, 1);
          const lastDay = new Date(currentYear, month + 1, 0);
          
          // Décalage pour commencer le mois le bon jour (Lundi=1)
          let startingDay = firstDay.getDay();
          if (startingDay === 0) startingDay = 7; // Dimanche devient 7
          startingDay -= 1; // Pour commencer à 0 (Lundi=0)
          
          // Jours du mois précédent
          const prevMonthLastDay = new Date(currentYear, month, 0).getDate();
          for (let i = 0; i < startingDay; i++) {
              const dayElement = document.createElement('div');
              dayElement.className = 'day other-month';
              dayElement.textContent = prevMonthLastDay - startingDay + i + 1;
              daysGrid.appendChild(dayElement);
          }
          
          // Jours du mois en cours
          const today = new Date();
          const isCurrentMonthAndYear = today.getFullYear() === currentYear && today.getMonth() === month;
          
          for (let day = 1; day <= lastDay.getDate(); day++) {
              const dayElement = document.createElement('div');
              dayElement.className = 'day';
              dayElement.textContent = day;
              
              const formattedDay = (day < 10) ? `0${day}` : `${day}`;
              const formattedMonth = (month + 1 < 10) ? `0${month + 1}` : `${month + 1}`;
              const dateValue = `${currentYear}-${formattedMonth}-${formattedDay}`;
              
              dayElement.setAttribute('data-date', dateValue);
              
              // Vérifier si c'est aujourd'hui
              if (isCurrentMonthAndYear && day === today.getDate()) {
                  dayElement.classList.add('today');
              }
              
              // Vérifier si c'est un week-end (Sam=5, Dim=6)
              const dayOfWeek = new Date(currentYear, month, day).getDay();
              if (dayOfWeek === 0 || dayOfWeek === 6) {
                  dayElement.classList.add('weekend');
              }
              
              // Vérifier si c'est un jour férié
              const dateKey = `${formattedMonth}-${formattedDay}`;
              
              if (holidays[dateKey]) {
                  dayElement.classList.add(holidays[dateKey].type === 1 ? 'holiday-type-1' : 'holiday-type-2');
              }
              
              daysGrid.appendChild(dayElement);
          }
          
          // Jours du mois suivant
          const totalCells = startingDay + lastDay.getDate();
          const remainingCells = 42 - totalCells; // 6 semaines * 7 jours
          
          for (let i = 1; i <= remainingCells; i++) {
              const dayElement = document.createElement('div');
              dayElement.className = 'day other-month';
              dayElement.textContent = i;
              daysGrid.appendChild(dayElement);
          }
          
          monthElement.appendChild(monthHeader);
          monthElement.appendChild(holidaysList);
          monthElement.appendChild(daysHeader);
          monthElement.appendChild(daysGrid);
          
          monthsContainer.appendChild(monthElement);
      }
      
      // Initialiser les événements de clic sur les jours
      initDayClickEvents();
  }
  
  function initDayClickEvents() {
      document.querySelectorAll('.day:not(.other-month)').forEach(day => {
          day.addEventListener('click', function() {
              const dateStr = this.getAttribute('data-date');
              if (dateStr) {
                  // Vérifier si ce jour est un jour férié ajouté par l'utilisateur
                  if (userAddedHolidays[dateStr]) {
                      openEditModal(dateStr, userAddedHolidays[dateStr]);
                  }
              }
          });
      });
  }
  
  function openEditModal(dateStr, holiday) {
      currentEditingHoliday = { date: dateStr, ...holiday };
      
      const modal = document.getElementById('holiday-modal');
      const modalTitle = document.getElementById('modal-title');
      const submitBtn = document.getElementById('submit-holiday-btn');
      const deleteBtn = document.getElementById('delete-holiday-btn');
      
      modalTitle.textContent = "Modifier jour férié";
      submitBtn.textContent = "Mettre à jour";
      deleteBtn.style.display = "block";
      
      // Remplir le formulaire avec les données existantes
      document.getElementById('holiday-name').value = holiday.name;
      document.getElementById('holiday-duration').value = 1; // Par défaut 1 jour pour la modification
      document.getElementById('holiday-date').value = dateStr;
      
      modal.style.display = 'block';
  }
  
  function openAddModal() {
      currentEditingHoliday = null;
      
      const modal = document.getElementById('holiday-modal');
      const modalTitle = document.getElementById('modal-title');
      const submitBtn = document.getElementById('submit-holiday-btn');
      const deleteBtn = document.getElementById('delete-holiday-btn');
      
      modalTitle.textContent = "Ajouter un jour férié religieux";
      submitBtn.textContent = "Ajouter le jour férié";
      deleteBtn.style.display = "none";
      
      // Réinitialiser le formulaire
      document.getElementById('holiday-form').reset();
      
      modal.style.display = 'block';
  }
  
  function deleteHoliday(dateStr) {
      if (userAddedHolidays[dateStr]) {
          delete userAddedHolidays[dateStr];
          localStorage.setItem('userAddedHolidays', JSON.stringify(userAddedHolidays));
          updateYear();
      }
  }
  
  // Gestion de l'ajout/modification de jours fériés
  function setupHolidayForm() {
      const modal = document.getElementById('holiday-modal');
      const btn = document.getElementById('add-holiday-btn');
      const span = document.getElementsByClassName('close')[0];
      const holidayForm = document.getElementById('holiday-form');
      const deleteBtn = document.getElementById('delete-holiday-btn');

      btn.onclick = openAddModal;

      span.onclick = function() {
          modal.style.display = 'none';
      };

      window.onclick = function(event) {
          if (event.target === modal) {
              modal.style.display = 'none';
          }
      };

      holidayForm.onsubmit = function(e) {
          e.preventDefault();

          const holidayName = document.getElementById('holiday-name').value;
          const holidayDuration = parseInt(document.getElementById('holiday-duration').value);
          const holidayDate = document.getElementById('holiday-date').value;

          if (!holidayName || !holidayDate) {
              alert('Veuillez remplir tous les champs');
              return;
          }

          const dateObj = new Date(holidayDate);
          const year = dateObj.getFullYear();
          const month = String(dateObj.getMonth() + 1).padStart(2, '0');
          const day = String(dateObj.getDate()).padStart(2, '0');

          // Si on est en mode édition, supprimer l'ancienne entrée
          if (currentEditingHoliday) {
              delete userAddedHolidays[currentEditingHoliday.date];
          }

          // Ajouter chaque jour de vacances
          for (let i = 0; i < holidayDuration; i++) {
              const currentDate = new Date(dateObj);
              currentDate.setDate(dateObj.getDate() + i);

              const currentYear = currentDate.getFullYear();
              const currentMonth = String(currentDate.getMonth() + 1).padStart(2, '0');
              const currentDay = String(currentDate.getDate()).padStart(2, '0');

              const dateKey = `${currentYear}-${currentMonth}-${currentDay}`;

              userAddedHolidays[dateKey] = {
                  name: holidayName,
                  type: 2 // Type 2 pour les fêtes religieuses (vert)
              };
          }

          // Sauvegarder dans le localStorage
          localStorage.setItem('userAddedHolidays', JSON.stringify(userAddedHolidays));

          // Mettre à jour le calendrier
          updateYear();

          // Fermer le modal et réinitialiser le formulaire
          modal.style.display = 'none';
          holidayForm.reset();
      };

      deleteBtn.onclick = function() {
          if (currentEditingHoliday && confirm("Voulez-vous vraiment supprimer ce jour férié ?")) {
              deleteHoliday(currentEditingHoliday.date);
              modal.style.display = 'none';
          }
      };
  }
  
  prevYearButton.addEventListener('click', function() {
      currentYear--;
      updateYear();
  });
  
  nextYearButton.addEventListener('click', function() {
      currentYear++;
      updateYear();
  });
  
  // Initialisation
  setupHolidayForm();
  updateYear();
});