document.addEventListener('DOMContentLoaded', function () {
            const tasksListContainer = document.getElementById('tasksList');

            const storedTasks = JSON.parse(localStorage.getItem('tasks')) || [];

            storedTasks.forEach(function (task) {
                const taskArticle = document.createElement('article');
                taskArticle.classList.add('task');

                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.id = task.name;
                checkbox.classList.add('task-checkbox');

                const taskDetails = document.createElement('div');
                taskDetails.classList.add('task-details');

                const taskHeader = document.createElement('header');
                const taskTitle = document.createElement('h2');
                taskTitle.textContent = task.name;

                const taskDescription = document.createElement('p');
                taskDescription.textContent = task.description;

                const taskDate = document.createElement('p');
                taskDate.textContent = 'Fecha límite: ' + task.date;

                const editButton = document.createElement('button');
                editButton.type = 'button';
                editButton.textContent = 'Editar';
                editButton.addEventListener('click', function () {
                    // Lógica para editar la tarea
                });

                taskHeader.appendChild(taskTitle);
                taskDetails.appendChild(taskHeader);
                taskDetails.appendChild(taskDescription);
                taskDetails.appendChild(taskDate);
                taskDetails.appendChild(editButton);

                taskArticle.appendChild(checkbox);
                taskArticle.appendChild(taskDetails);

                tasksListContainer.appendChild(taskArticle);
            });
        });