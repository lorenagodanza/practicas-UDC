document.addEventListener('DOMContentLoaded', function () {
            const addTaskButton = document.getElementById('addTaskButton');

            addTaskButton.addEventListener('click', function (event) {
                event.preventDefault();

                const taskName = document.getElementById('nombre').value;
                const taskDescription = document.getElementById('descripcion').value;
                const taskDate = document.getElementById('fecha').value;

                if (taskName.trim() === '') {
                    alert('Por favor, ingrese el nombre de la tarea.');
                    return;
                }

                 // Validar que la fecha no sea anterior a hoy
                const today = new Date();
                const selectedDate = new Date(taskDate);

                if (selectedDate < today) {
                alert('La fecha límite no puede ser anterior a hoy.');
                return;
                }

                const newTask = {
                    name: taskName,
                    description: taskDescription,
                    date: taskDate,
                    completed: false
                };

                const existingTasks = JSON.parse(localStorage.getItem('tasks')) || [];
                existingTasks.push(newTask);

                localStorage.setItem('tasks', JSON.stringify(existingTasks));
                 alert('¡La tarea se ha añadido correctamente!');

                // Redirigir a ambas páginas después de agregar la tarea
                location.href = 'activas.html';

            });
        });