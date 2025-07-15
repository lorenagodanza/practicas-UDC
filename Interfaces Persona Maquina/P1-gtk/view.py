import html

import gi

gi.require_version('Gtk', '4.0')
from typing import Callable,Protocol


from gi.repository import Gtk, GObject, GdkPixbuf
import gettext

_ = gettext.gettext
N_ = gettext.ngettext



class DrinkObject(GObject.GObject): #representa objetos de tipo Bebida
    def __init__(self, name):
        super().__init__()
        self._name = name
        self._id=id


    @GObject.Property(type=str)
    def name(self):
        return self._name

    @GObject.Property(type=str)
    def id(self):
        return self._id

    #metodo para representar objetos de bebida en una cadena legible
    def __repr__(self):
        return f"DrinkObject(name={self._name},id={self._id})"

#Este método crea una instancia de la clase Gtk.Application y conectamos la señal 'activate' con
#la funcion on_activate
def run(application_id: str, on_activate: Callable) -> None:
    app = Gtk.Application(application_id=application_id)
    app.connect('activate', on_activate)
    app.run()



class ViewHandler(Protocol):
    def on_search_clicked(name: str) -> None: pass
    def on_category_changed(category: str) -> None:
        pass
    def on_ingredient_changed(ingredient: str) -> None: pass


class View:


    def __init__(self):  #método constructor
        self.handler=None
        self.selected_drink=None  #almacena bebida seleccionada
        self.drink_name_to_id = {}
        self.drink_id_to_name = {}
        self.drink_list = []
        self.drink_image = Gtk.Image()
        self.ingredient_names = {} #Almacena los nombres de los ingredientes usados al buscar
        self.ingredient_measure_list = [] #Alamacena los ingredientes y medidas de una bebida
        self.category_combobox = Gtk.ComboBoxText()
        self.ingredient_combobox = Gtk.ComboBoxText()
        self.placeholder_image_path = "./placeholder.PNG"

    def set_handler(self, handler: ViewHandler ) -> None:
        self.handler = handler

    def on_activate(self, app: Gtk.Application) -> None: #se llama cuando se activa la aplicación y crea la interfaz
        self.crearInterfaz(app)
        self.search_bar.connect('changed', self.on_search_bar_changed)
        # Desactivar el botón de búsqueda al inicio
        self.search_button.set_sensitive(False)


    def crearInterfaz(self, app: Gtk.Application) -> None:
        self.window = win = Gtk.ApplicationWindow(title=_("Drink finder"))
        win.set_default_size(800, 600)
        app.add_window(win)
        win.connect("destroy", lambda win: win.close())

        main_box = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=12) #
        main_box.set_margin_top(12)
        main_box.set_margin_start(12)
        main_box.set_margin_end(12)
        main_box.set_margin_bottom(12)

        paned = Gtk.Paned(orientation=Gtk.Orientation.HORIZONTAL)
        paned.set_position(300)

        search_box = Gtk.Box(orientation=Gtk.Orientation.HORIZONTAL, spacing=12)  #creamos un cuadro de búsqueda
        self.search_bar = Gtk.SearchEntry(
            placeholder_text=_("Search here..."),
            width_chars=20,
            margin_start=15,
            margin_end=0,
        )

        self.search_button = Gtk.Button(label=_("Search"), margin_start=0, margin_end=15) #creamos el botón de búsqueda

        # cuando se haga click en el botón se ejecutará self.handler.on_search_clicked
        self.search_button.connect('clicked', lambda _wg: self.handler.on_search_clicked(self.search_bar.get_text()))
        self.spinner = Gtk.Spinner()

        search_box.append(self.search_bar)
        search_box.append(self.search_button)
        search_box.append(self.spinner)
        main_box.append(search_box)

        # Agregar ComboBoxText para categorías
        category_box = Gtk.Box(orientation=Gtk.Orientation.HORIZONTAL, spacing=12)
        category_label = Gtk.Label(label=_("Categories:"))
        category_label.set_margin_end(6)  # Ajusta el margen de la etiqueta si es necesario
        self.category_combobox.connect('changed', self.on_category_changed)
        category_box.append(category_label)
        category_box.append(self.category_combobox)
        main_box.append(category_box)

        # ComboBoxText para ingredientes
        ingredient_box = Gtk.Box(orientation=Gtk.Orientation.HORIZONTAL, spacing=12)
        ingredient_label = Gtk.Label(label=_("Ingredients:"))
        ingredient_label.set_margin_end(6)
        self.ingredient_combobox.connect('changed', self.on_ingredient_changed)
        ingredient_box.append(ingredient_label)
        ingredient_box.append(self.ingredient_combobox)
        main_box.append(ingredient_box)



        self.data = Gtk.Label(
            label="",
            halign=Gtk.Align.START,
            vexpand=True,
            margin_start=20,
            margin_top=5
        )
        self.scroll = Gtk.ScrolledWindow(
            has_frame=True,
            hscrollbar_policy=Gtk.PolicyType.AUTOMATIC,
            vscrollbar_policy=Gtk.PolicyType.AUTOMATIC,
            margin_top=10,
            margin_end=10,
            margin_bottom=10,
            margin_start=10,
        )
        self.scroll.set_min_content_width(300)
        self.scroll.set_min_content_height(600)

        #Creamos un ScrolledWindow para la información de la bebida
        self.drink_scroll = Gtk.ScrolledWindow(
            has_frame=True,
            hscrollbar_policy=Gtk.PolicyType.AUTOMATIC,
            vscrollbar_policy=Gtk.PolicyType.AUTOMATIC,
            margin_top=10,
            margin_end=10,
            margin_bottom=10,
            margin_start=10,
        )
        self.drink_scroll.set_min_content_width(300)
        self.drink_scroll.set_min_content_height(600)


        #Creamos el contenedor que va a ir en el scroll de la bebida
        self.descriptions_container = Gtk.Box(orientation=Gtk.Orientation.VERTICAL, spacing=10)

        #Creamos la etiqueta que va a mostrar el nombre de la bebida
        self.description_label = Gtk.Label(
            label="",
            halign=Gtk.Align.START,
            vexpand=True,
            margin_start=20,
            margin_top=5
        )
        self.descriptions_container.append(self.description_label)


        #Establecemos el tamaño de la imagen de la bebida
        new_width = 400
        new_height = 400

        self.drink_image.set_size_request(new_width, new_height)

        self.descriptions_container.append(self.drink_image)

        #Creamos un scroll para la lista de ingredientes de la bebida
        self.scroll_ingr = Gtk.ScrolledWindow(
            has_frame=True,
            hscrollbar_policy=Gtk.PolicyType.AUTOMATIC,
            vscrollbar_policy=Gtk.PolicyType.AUTOMATIC,
            margin_top=10,
            margin_end=10,
            margin_bottom=10,
            margin_start=10,
        )
        self.scroll_ingr.set_min_content_width(100)
        self.scroll_ingr.set_min_content_height(200)

        #Creamos la lista de los ingredientes de la bebida
        self.ingredient_measure_list = Gtk.ListBox()
        #Conectamos la lista de ingredientes de la bebida con la función para mostrar los detalles
        self.ingredient_measure_list.connect("row-activated", self.on_ingredient_selected)

        self.scroll_ingr.set_child(self.ingredient_measure_list)

        self.descriptions_container.append(self.scroll_ingr)

        #Información sobre el ingrediente con scroll
        self.scrolled_ingredient_text = Gtk.ScrolledWindow(
            has_frame=True,
            hscrollbar_policy=Gtk.PolicyType.NEVER,
            vscrollbar_policy=Gtk.PolicyType.AUTOMATIC,
            margin_top=10,
            margin_end=10,
            margin_bottom=10,
            margin_start=10,

        )
        self.scrolled_ingredient_text.set_min_content_width(100)
        self.scrolled_ingredient_text.set_min_content_height(200)
        self.ingredient_text_view = Gtk.TextView()
        self.ingredient_text_view.set_wrap_mode(Gtk.WrapMode.WORD_CHAR)  # para cortar texto co tamaño da ventana
        self.ingredient_text_buffer = self.ingredient_text_view.get_buffer()
        self.scrolled_ingredient_text.set_child(self.ingredient_text_view)

        # Información sobre el modo de preparación con scroll
        self.scrolled_instructions_text = Gtk.ScrolledWindow(
            has_frame=True,
            hscrollbar_policy=Gtk.PolicyType.NEVER,
            vscrollbar_policy=Gtk.PolicyType.AUTOMATIC,
            margin_top=10,
            margin_end=10,
            margin_bottom=10,
            margin_start=10,
        )
        self.scrolled_instructions_text.set_min_content_width(100)
        self.scrolled_instructions_text.set_min_content_height(200)
        self.instructions_text_view = Gtk.TextView()
        self.instructions_text_view.set_wrap_mode(Gtk.WrapMode.WORD_CHAR)  # Para cortar co tamaño da ventana
        self.instructions_text_buffer = self.instructions_text_view.get_buffer()
        self.scrolled_instructions_text.set_child(self.instructions_text_view)


        #Añadimos la lista de ingredientes y las instrucciones
        self.descriptions_container.append(self.scrolled_ingredient_text)
        self.descriptions_container.append(self.scrolled_instructions_text)

        #Metemos el contendor de la información de la bebida en la ScrolledWindow
        self.drink_scroll.set_child(self.descriptions_container)

        #Añadimos la ScrolledWindow de la bebida al Paned
        paned.set_end_child(self.drink_scroll)

        #Creamos la lista de bebidas
        self.drink_list = Gtk.ListBox()
        #Conectamos la lista de bebidas con la función que actualizará los datos de la bebida que se muestra
        self.drink_list.connect("row-activated", self.on_drink_selected)

        self.scroll.set_child(self.drink_list)

        paned.set_start_child(self.scroll)

        main_box.append(paned)

        win.set_child(main_box)
        win.present()

    def on_search_bar_changed(self, widget):
        text = self.search_bar.get_text()
        self.search_button.set_sensitive(bool(text))

    def update_drink_list(self, data):  #actualiza la lista de bebidas
        # Limpiar el diccionario
        self.drink_name_to_id.clear()
        #ordena alfabeticamente
        data = sorted(data, key=lambda drink_data: drink_data["name"])

        new_drink_list = Gtk.ListBox()
        new_drink_list.connect("row-activated", self.on_drink_selected) #cuando se selecciona una bebida de la lista se activa on_drink_selected

        for drink_data in data:
            drink_name = drink_data["name"] #obtenemos nombre
            drink_id = drink_data["id"]  #obtenemos id
            self.drink_name_to_id[drink_name] = drink_id #almacenamos en el diccionario
            self.drink_id_to_name[drink_id] = drink_name
            label = Gtk.Label(label=drink_name)
            new_drink_list.append(label)
        self.drink_list = new_drink_list
        self.scroll.set_child(self.drink_list)
        if self.search_bar.get_text().strip():  # Comprueba si la barra de búsqueda no está vacía
            self.search_button.set_sensitive(True)
        else:
            self.search_button.set_sensitive(False)

        self.spinner.stop()
        self.search_bar.set_sensitive(True)

    def update_ingredient_list(self, ingr_me, ingredients):
        #Limpiamos el diccionario de asignaciones ingrediente, medida -> valor
        self.ingredient_names.clear()
        #Creamos una lista nueva y le asociamos la función que muestra los detalles del ingrediente
        new_ingredient_measure_list = Gtk.ListBox()
        new_ingredient_measure_list.connect("row-activated",
                               self.on_ingredient_selected)


        #Añadimos los ingredientes a la lista y al diccionario
        for i in range(len(ingr_me)):
            label = Gtk.Label(label=ingr_me[i])
            self.ingredient_names[ingr_me[i]] = ingredients[i]
            new_ingredient_measure_list.append(label)

        #Actualizamos la lista y la añadimos a la vista
        self.ingredient_measure_list = new_ingredient_measure_list
        self.scroll_ingr.set_child(self.ingredient_measure_list)


    def update_categories(self, categories):
        self.category_combobox.remove_all()  # Elimina las categorías anteriores
        # Ordena alfabéticamente la lista de categorías antes de agregarlas al ComboBoxText
        sorted_categories = sorted(categories)
        for category in sorted_categories:
            self.category_combobox.append_text(category) #agraga categorias al desplegable


    #Actualizamos la lista de ingredientes
    def update_ingredients(self, ingredients):
        self.ingredient_combobox.remove_all()
        # Ordena alfabéticamente la lista de ingredientes antes de agregarlos al ComboBoxText
        sorted_ingredients = sorted(ingredients)

        for ingredient in sorted_ingredients:
            self.ingredient_combobox.append_text(ingredient)

    def update_drink_description(self, drink_id,drink_details,use_spanish,use_german):
        if drink_details != {}:
            ingredients = drink_details.get("ingredients", [])
            ingredients_measures = drink_details.get("ingredientsMeasures", [])
            image_url=drink_details.get("strDrinkThumb",[])
            self.drink_image.set_from_file(self.placeholder_image_path)
            self.handler.get_image(image_url)
            # Formatear solo el nombre del cóctel en negrita
            drink_name = self.drink_id_to_name.get(drink_id)
            name_in_bold = f"<b>{html.escape(drink_name)}</b>"
            self.description_label.set_markup(name_in_bold)
            self.ingredient_list = ingredients
            self.ingredient_measure_list = ingredients_measures

            self.ingredient_text_buffer.set_text("")

            if use_spanish:
                instructions_text = drink_details.get("strInstructionsES", "")
            elif use_german:
                instructions_text = drink_details.get("strInstructionsDE", "")
            else:
                instructions_text = drink_details.get("strInstructions", "")

            # si non hai instruccións no idioma seleccionado
            if not instructions_text or instructions_text.strip() == "":
                instructions_text = drink_details.get("strInstructions", "")

            # si tampouco hai instruccións en inglés
            if not instructions_text or instructions_text.strip() == "":
                instructions_text = _("No instructions provided")

            self.instructions_text_buffer.set_text(_("INSTRUCTIONS:") + "\n\n" + instructions_text)

            self.update_ingredient_list(ingredients_measures, ingredients)
            self.spinner.stop()

    def on_drink_selected(self, widget, row):
        if row != None:
            drink_name = row.get_child().get_text()  #obtiene el texto del elemento seleccionado
            print("Selected Drink Name:", drink_name)

            drink_id = self.drink_name_to_id.get(drink_name) #busca el id en el diccionario a partir del nombre

            if drink_id:
                self.handler.get_drink_details(drink_id)




    def on_ingredient_selected(self, widget, row):
        if row:
            ingredient_text = row.get_child().get_text()  #obtiene el texto del elemento seleccionado
            ingredient_name = self.ingredient_names.get(ingredient_text)  # busca el nombre en el diccionario a partir del texto
            print("Selected Ingredient Name:", ingredient_name)
            # Obtiene la desdcripción del ingrediente
            self.handler.get_ingredient_details(ingredient_name)



    def update_ingredient_description(self, ingredient_details):
        # Obtiene la descripción del ingrediente

        if ingredient_details == {}:
            description = ""
        else:
            # Mostramos el la descripción como nombre + descripción
            name = ingredient_details.get("name", "")
            description_text = ingredient_details.get("description", "")
            if description_text is None:
                description_text = _("No description available")
            upper_name = (name + ":\n").upper()
            description = f"{upper_name}\n{description_text}"

        self.ingredient_text_buffer.set_text(description)
        self.spinner.stop()


    def on_category_changed(self, widget):
        category = widget.get_active_text()
        self.handler.on_category_changed(category)



    def on_ingredient_changed(self, widget):
        ingredient = widget.get_active_text()
        self.handler.on_ingredient_changed(ingredient)



    def update_image(self, image_data):

        if image_data is not None:
            loader = GdkPixbuf.PixbufLoader.new()
            loader.write(image_data)
            loader.close()

            pixbuf = loader.get_pixbuf()

            if pixbuf:
                pixbuf.new_width = 250  # Cambia el ancho
                pixbuf.new_height = 250  # Cambia la altura

                # Establecer el Pixbuf escalado en Gtk.Image
                self.drink_image.set_from_pixbuf(pixbuf)
            else:
                print("Error: No se pudo cargar la imagen correctamente.")

        else:
            print("Error: No se pudo cargar la imagen correctamente.")

        self.spinner.stop()



    def show_error_message(self, text : str):
        dialog = Gtk.MessageDialog(
            transient_for=self.window,
            modal=True,
            message_type=Gtk.MessageType.ERROR,
            buttons=Gtk.ButtonsType.OK,
            text=_("Error"),
            secondary_text=text,

        )
        dialog.connect('response', lambda d, _: self.on_error_dialog_response(d))
        dialog.show()

    def on_error_dialog_response(self, dialog):
        dialog.destroy()
        self.spinner.stop()
        self.search_bar.set_sensitive(True)  # Habilita la barra de búsqueda
        self.search_button.set_sensitive(True)  # También puedes habilitar el botón de búsqueda si es necesario


