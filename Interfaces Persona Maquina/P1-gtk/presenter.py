import time

from view import View,run
from model import Model
import threading
from gi.repository import GLib
from model import ServerError
from model import NotFoundError

import gettext

import locale
#miramos en que idioma estamos
locale_language, _ = locale.getdefaultlocale()
if locale_language.startswith('es'):
    use_spanish = True
    use_german = False
elif locale_language.startswith('de'):
    use_german = True
    use_spanish = False
else:
    use_spanish = False
    use_german = False


_ = gettext.gettext
N_ = gettext.ngettext


class Presenter:
    def __init__(self, model: Model, view: View):
        self.model = model
        self.view = view


    def run(self, application_id: str):
        self.view.set_handler(self)
        categories = []
        ingredients = []
        try:
            categories = self.model.get_categories()  # Obtener las categorías desde el modelo
            ingredients = self.model.get_ingredients()  # Obtener los ingredientes desde el modelo

        except ServerError:
            GLib.idle_add(self.view.show_error_message, _("Unexpected Server Response"))
            threading.Thread(target=self.load_comboboxes, daemon=True).start()
        except TimeoutError:
            GLib.idle_add(self.view.show_error_message, _("Waiting time exceeded"))
            threading.Thread(target=self.load_comboboxes, daemon=True).start()
        except ConnectionError:
            GLib.idle_add(self.view.show_error_message, _("Internet Connection Error"))
            threading.Thread(target=self.load_comboboxes, daemon=True).start()
        finally:
            self.view.update_categories(categories)  # Actualiza las categorías en la vista
            self.view.update_ingredients(ingredients)  # Actualiza los ingredientes en la vista
            run(application_id=application_id, on_activate=self.view.on_activate)

    def load_comboboxes(self):
        while not self.model.check_internet_connection():
            time.sleep(1)
        categories = self.model.get_categories()  # Obtener las categorías desde el modelo
        ingredients = self.model.get_ingredients()  # Obtener los ingredientes desde el modelo
        self.view.update_categories(categories)  # Actualiza las categorías en la vista
        self.view.update_ingredients(ingredients)  # Actualiza los ingredientes en la vista


    def on_search_clicked(self, name: str) -> None:
        if name is not None:
            name = name.strip()
            self.view.category_combobox.set_active(-1)  # restablece os combo
            self.view.ingredient_combobox.set_active(-1)
            threading.Thread(target=self.search_name, args=(name,), daemon=True).start()


    def search_name(self, name: str) -> None:
        try:
            GLib.idle_add(self._waiting, True)
            result = self.model.do_search_name(name)
            GLib.idle_add(self.update_drink,result)
        except NotFoundError:
            GLib.idle_add(self.view.show_error_message, _("Cocktail not found"))
        except ServerError:
            GLib.idle_add(self.view.show_error_message, _("Unexpected Server Response"))
        except TimeoutError:
            GLib.idle_add(self.view.show_error_message, _("Waiting time exceeded"))
        except ConnectionError:
            GLib.idle_add(self.view.show_error_message, _("Internet Connection Error"))


    def _waiting(self, deactivate=True) -> None:
        self.view.spinner.start()
        if deactivate:
            self.view.search_button.set_sensitive(False)
            self.view.search_bar.set_sensitive(False)

    def update_drink(self, result: str) -> None:
        self.view.update_drink_list(result)

    def update_drink_details(self, id_drink:str,result: str) -> None:

        self.view.update_drink_description(id_drink,result,use_spanish,use_german)

    def work_drink_details(self, drink_id: str) -> None:
        try:
            GLib.idle_add(self._waiting, False)
            if use_spanish:
                result = self.model.get_drink_details(drink_id, "es")
            elif use_german:
                result = self.model.get_drink_details(drink_id, "de")
            else:
                result = self.model.get_drink_details(drink_id, None)

            GLib.idle_add(self.update_drink_details, drink_id, result)
        except NotFoundError:
            GLib.idle_add(self.view.show_error_message, _("Cocktail not found"))
        except ServerError:
            GLib.idle_add(self.view.show_error_message, _("Unexpected Server Response"))
        except TimeoutError:
            GLib.idle_add(self.view.show_error_message, _("Waiting time exceeded"))
        except ConnectionError:
            GLib.idle_add(self.view.show_error_message, _("Internet Connection Error"))


    #Obtiene los detalles de la bebida seleccionada
    def get_drink_details(self, drink_id: str):
        threading.Thread(target=self.work_drink_details, args=(drink_id,), daemon=True).start()



    def update_ingr(self, result: str) -> None:
        self.view.update_ingredient_description(result)

    def work_ingr(self, name: str) -> None:
        try:
            GLib.idle_add(self._waiting, False)
            result = self.model.get_ingredient_details(name)
            GLib.idle_add(self.update_ingr, result)
        except NotFoundError:
            GLib.idle_add(self.view.show_error_message, _("Cocktail not found"))
        except ServerError:
            GLib.idle_add(self.view.show_error_message, _("Unexpected Server Response"))
        except TimeoutError:
            GLib.idle_add(self.view.show_error_message, _("Waiting time exceeded"))
        except ConnectionError:
            GLib.idle_add(self.view.show_error_message, _("Internet Connection Error"))



    #Obtiene los detalles de un ingrediente
    def get_ingredient_details(self, ingredient_name: str):
        threading.Thread(target=self.work_ingr, args=(ingredient_name,), daemon=True).start()

    def get_image(self, url):
        threading.Thread(target=self.work_image, args=(url,), daemon=True).start()

    def update_image(self, result: str) -> None:
        self.view.update_image(result)
    #Obtener la imagen
    def work_image(self, name: str):
        try:
            GLib.idle_add(self._waiting, False)
            result = self.model.get_image(name)
            GLib.idle_add(self.update_image, result)

        except ServerError:
            GLib.idle_add(self.view.show_error_message, _("Unexpected Server Response"))
        except TimeoutError:
            GLib.idle_add(self.view.show_error_message, _("Waiting time exceeded"))
        except ConnectionError:
            GLib.idle_add(self.view.show_error_message, _("Internet Connection Error"))


    #Obtiene la lista de bebidas cuando se selecciona una categoria
    def on_category_changed(self, category: str):
        if self.view.category_combobox.get_active() != -1:
            # Borra el contenido de la barra de búsqueda
            self.view.search_bar.set_text("")
            self.view.search_button.set_sensitive(False)
            self.view.ingredient_combobox.set_active(-1)
            if category is not None:
                threading.Thread(target=self.search_by_category, args=(category,), daemon=True).start()



    def search_by_category(self, category):
        try:
            GLib.idle_add(self._waiting, True)
            result = self.model.get_drinks_by_category(category)
            GLib.idle_add(self.update_drink, result)
        except ServerError:
            GLib.idle_add(self.view.show_error_message, _("Unexpected Server Response"))
        except TimeoutError:
            GLib.idle_add(self.view.show_error_message, _("Waiting time exceeded"))
        except ConnectionError:
            GLib.idle_add(self.view.show_error_message, _("Internet Connection Error"))




    #Obtiene la nueva lista de bebidas cuando cambiamos de ingrediente
    def on_ingredient_changed(self, ingredient):
        if self.view.ingredient_combobox.get_active() != -1:
            self.view.search_bar.set_text("")
            self.view.category_combobox.set_active(-1)
            self.view.search_button.set_sensitive(False)
            if ingredient is not None:
                threading.Thread(target=self.search_by_ingredient, args=(ingredient,), daemon=True).start()


    def search_by_ingredient(self, ingredient):
        try:
            GLib.idle_add(self._waiting, True)
            result = self.model.get_drinks_by_ingredient(ingredient)
            GLib.idle_add(self.update_drink, result)
        except ServerError:
            GLib.idle_add(self.view.show_error_message, _("Unexpected Server Response"))

        except TimeoutError:
            GLib.idle_add(self.view.show_error_message, _("Waiting time exceeded"))
        except ConnectionError:
            GLib.idle_add(self.view.show_error_message, _("Internet Connection Error"))












