import re
import requests
from requests.exceptions import Timeout
import time


class ServerError(Exception):
    pass
class NotFoundError(Exception):
    pass
class Model:
    def __init__(self):
        self.categories = []  # Lista para almacenar las categorias
        self.ingredients = [] # Lista para almacenar los ingredientes
        self.translations = {

            "es": {
                " ": " ",
                ", orange": ", naranja",
            "(seltzer water)": "(agua mineral con gas)",
            "-inch": " pulgadas,",
            "a little bit of": "un poco de",
            "about": "Sobre",
            "add": "Añade",
            "and": "y",
            "around rim put": "alrededor del borde",
            "bacardi": "Bacardí,",
            "bittersweet": "agridulce,",
            "beaten": "batido,",
            "black": "negro,",
            "blue": "azul,",
            "bottle": "botella",
            "bottles": "botellas",
            "by taste": "al gusto",
            "can": "lata",
            "carbonated": "carbonatada,",
            "chilled": "enfriado,",
            "chopped": "cortado,",
            "cropped": "recortado,",
            "chunk": "pedazo,",
            "cl": "cl",
            "cold": "frío,",
            "coarse": "grueso,",
            "cracked": "roto,",
            "crushed": "aplastado,",
            "cube": "cubo",
            "cubes": "cubos",
            "cup": "taza",
            "cups": "tazas",
            "dark": "oscuro,",
            "dash": "pizca",
            "dashes": "pizcas",
            "dl": "dl",
            "dried": "seco,",
            "drop": "gota",
            "drops": "gotas",
            "dry": "seco,",
            "fifth": "quinto,",
            "fill to top": "llenar hasta arriba",
            "fill to top with": "llenar hasta arriba con",
            "fill with": "llenar con",
            "fine": "fino",
            "finely": "bien",
            "flot bacardi": "Flot Bacardí",
            "for topping": "como topping",
            "fresh": "fresco,",
            "frozen": "congelado,",
            "full": "lleno,",
            "gal": "galón",
            "garnish": "guarnicionar",
            "garnish with": "guarnicionar con",
            "glass": "vaso",
            "gr": "gr",
            "granulated": "granulado,",
            "grated": "rallado,",
            "green": "verde,",
            "ground": "molido,",
            "handful": "puñado",
            "hard": "duro",
            "hot": "caliente",
            "iced": "helado",
            "inch": "pulgada",
            "instant": "instantáneo",
            "jigger": "medida",
            "jiggers": "medidas",
            "juice of": "zumo de",
            "l": "L",
            "large": "grande",
            "lb": "libra",
            "light": "light",
            "long": "largo",
            "lots": "un montón",
            "mild": "templado,",
            "milkey": "lechero,",
            "mini": "mini",
            "ml": "ml",
            "muscatel": "Muscatel",
            "one-inch": "una pulgada,",
            "or": "o",
            "over": "sobre",
            "oz": "onza",
            "package": "paquete",
            "part": "parte",
            "parts": "partes",
            "pinch": "pizca",
            "pint": "pinta",
            "pints": "pintas",
            "piece": "pieza",
            "pieces": "piezas",
            "plain": "sencillo",
            "pod": "cápsula",
            "pods": "cápsulas",
            "powdered": "en polvo,",
            "pure": "puro,",
            "qt": "cuarto de galón,",
            "quart": "cuarto",
            "red": "rojo,",
            "ripe": "maduro,",
            "roasted": "asado,",
            "scoop": "cucharada",
            "scoops": "cucharadas",
            "semi-sweet": "semi dulce,",
            "skimmed": "desnatado,",
            "slice": "rodaja",
            "slices": "rodajas",
            "small": "pequeño,",
            "shot": "chupito",
            "shots": "chupitos",
            "squeeze": "chorro",
            "sprig": "ramita",
            "sprigs": "ramitas",
            "stick": "palo",
            "sticks": "palos",
            "strip": "tira",
            "strips": "tiras",
            "strong": "fuerte,",
            "superfine": "super fino,",
            "sweet": "dulce,",
            "sweetened": "azucarado,",
            "splash": "toque",
            "splashes": "toques",
            "tablespoon": "cucharada",
            "tablespoons": "cucharadas",
            "tblsp": "cucharada",
            "top": "Coronar con",
            "top it up with": "rellénalo con",
            "top up": "Completar con",
            "top up with": "completar con",
            "to taste": "al gusto",
            "tropical": "tropical,",
            "tsp": "cucharada",
            "twist of": "toque de",
            "unsweetened": "sin azucarar,",
            "very": "muy",
            "wedge": "porción",
            "wedges": "porciones",
            "white": "blanco",
            "whole": "entero",
            "yellow": "amarillo"
            },

            "de": {
                " ": " ",
                ", orange": ", orange",
            "(seltzer water)": "(Mineralwasser)",
            "-inch": " inch,",
            "a little bit of": "ein bisschen von",
            "about": "Um",
            "add": "Hinzufügen",
            "and": "und",
            "around rim put": "um den Rand legen",
            "bacardi": "Bacardi,",
            "bittersweet": "bittersüß,",
            "beaten": "geschlagene,",
            "black": "schwarz,",
            "blue": "blau,",
            "bottle": "flasche",
            "bottles": "Flaschen",
            "by taste": "schmecken",
            "can": "dürfen",
            "carbonated": "mit Kohlensäure,",
            "chilled": "abgekühlt,",
            "chopped": "gehackt,",
            "cropped": "beschnitten,",
            "chunk": "Stück,",
            "cl": "cl",
            "cold": "kalt,",
            "coarse": "grob,",
            "cracked": "gebrochen,",
            "crushed": "zerquetscht,",
            "cube": "Würfel",
            "cubes": "Würfel",
            "cup": "Tasse",
            "cups": "Tassen",
            "dark": "dunkel,",
            "dash": "Prise",
            "dashes": "Prisen",
            "dl": "dl",
            "dried": "getrocknet,",
            "drop": "Tropfen",
            "drops": "Tropfen",
            "dry": "trocken,",
            "fifth": "fünftel,",
            "fill to top": "bis zum Rand füllen",
            "fill to top with": "bis zum Rand damit füllen",
            "fill with": "füllen mit",
            "fine": "fein",
            "finely": "fein",
            "flot bacardi": "Flot Bacardi",
            "for topping": "als Belag",
            "fresh": "frisch,",
            "frozen": "gefroren,",
            "full": "voll,",
            "gal": "Gallone",
            "garnish": "garnieren",
            "garnish with": "garnieren mit",
            "glass": "Glas",
            "gr": "gr",
            "granulated": "granuliert,",
            "grated": "gerieben,",
            "green": "Grün,",
            "ground": "gemahlen,",
            "handful": "Handvoll",
            "hard": "hart",
            "hot": "heiß",
            "iced": "vereist",
            "inch": "inch",
            "instant": "sofortig",
            "jigger": "jigger",
            "jiggers": "jigger",
            "juice of": "Saft von",
            "l": "L",
            "large": "groß",
            "lb": "Pfund",
            "light": "light",
            "long": "lang",
            "lots": "viel",
            "mild": "lau,",
            "milkey": "milchig,",
            "mini": "mini",
            "ml": "ml",
            "muscatel": "Muscatel",
            "one-inch": "ein Zoll,",
            "or": "oder",
            "over": "über",
            "oz": "Unze",
            "package": "Paket",
            "part": "Teil",
            "parts": "Teile",
            "pinch": "Prise",
            "pint": "Pint",
            "pints": "Pints",
            "piece": "Stück",
            "pieces": "Stücke",
            "plain": "schmucklos",
            "pod": "Hülse",
            "pods": "Hülsen",
            "powdered": "pulverisiert,",
            "pure": "rein,",
            "qt": "qt,",
            "quart": "Quart",
            "red": "Rot,",
            "ripe": "reif,",
            "roasted": "geröstet,",
            "scoop": "Esslöffel",
            "scoops": "Esslöffel",
            "semi-sweet": "halbsüß,",
            "skimmed": "abgeschöpft,",
            "slice": "Scheibe",
            "slices": "Scheiben",
            "small": "klein,",
            "shot": "Schuss",
            "shots": "Schüsse",
            "squeeze": "Spritzer",
            "sprig": "Zweig",
            "sprigs": "Zweige",
            "stick": "Stock",
            "sticks": "Stöcke",
            "strip": "Streifen",
            "strips": "Streifen",
            "strong": "stark,",
            "superfine": "superfein,",
            "sweet": "süß,",
            "sweetened": "gesüßt,",
            "splash": "Spritzen",
            "splashes": "Spritzer",
            "tablespoon": "Esslöffel",
            "tablespoons": "Esslöffel",
            "tblsp": "Esslöffel",
            "top": "toppen",
            "top it up with": "auffüllen",
            "top up": "auffüllen",
            "top up with": "auffüllen mit",
            "to taste": "schmecken",
            "tropical": "tropisch,",
            "tsp": "Esslöffel",
            "twist of": "Hauch von",
            "unsweetened": "ungesüßt,",
            "very": "sehr",
            "wedge": "Stück",
            "wedges": "Stücken",
            "white": "Weiß",
            "whole": "ganz",
            "yellow": "Gelb"
            }

        }



    def translate_measure(self, measure, language):
        numbers = r'(\d+(?:[-./]\d+)?)'

        elements = re.split(numbers, measure)

        translated_measure = ""

        for element in elements:
            translated_measure = translated_measure + " " + self.translate_piece(element.strip(), language)

        return translated_measure


    def translate_piece(self, piece, language):

        numbers = r'\d+(?:[-./]\d+)?'

        if re.match(numbers, piece):

            return piece

        elif piece == " " or piece == "":
            return ""

        else:
            if piece in self.translations[language]:
                return self.translations[language][piece]

            else:
                pieces = piece.split()

                if pieces[0] == piece:
                    return piece
                else:
                    last_piece = pieces[-1]
                    rest_pieces = ' '.join(pieces[:-1])
                    return self.translate_piece(rest_pieces.strip(), language) + " " + self.translate_piece(last_piece.strip(), language)




    #Funcion para comprobar si el usuario tiene conexión a Internet
    def check_internet_connection(self):
        try:
            # Intentamos realizar una solicitud a la URL de Google
            response = requests.get("https://www.google.com", timeout=5)
            if response.status_code == 200:
                return True  #El usuario tiene conexión
            else:
                return False  #Error al conectar con el servidor
        except requests.ConnectionError:
            return False  #El usuario no tiene conexión a Internet

    #Obtiene las categorías de la API
    def get_categories(self) -> list:
        try:
            # Realizar una solicitud GET a la API de categorías
            url = "https://www.thecocktaildb.com/api/json/v1/1/list.php?c=list"
            response = requests.get(url, timeout=5)

            # Verificar si la solicitud fue exitosa (código de respuesta 200)
            if response.status_code == 200:
                data = response.json()

                # Verificar si la respuesta contiene datos de categorías
                if "drinks" in data:
                    self.categories = [category["strCategory"] for category in data["drinks"]]
                    return self.categories
                else:
                    raise ServerError
            else:
                print("Error " + str(response.status_code) + " en la obtención de los ingredientes")
                raise ServerError
        except Timeout:
            print("Exceeded waiting time")
            raise TimeoutError
        except requests.exceptions.ConnectionError:
            print("No internet connection")
            raise ConnectionError
        except Exception as e:
            print("Error en la obtención de los ingredientes: " + str(e))
            raise ServerError

    def get_drinks_by_category(self, category):

        # Define la URL de la API para buscar bebidas por categoría
        url = f"https://www.thecocktaildb.com/api/json/v1/1/filter.php?c={category}"

        try:
            # Realiza la solicitud GET a la API
            response = requests.get(url, timeout=5)
            response.raise_for_status()  # Lanza una excepción si la solicitud no fue exitosa

            # Analiza los datos JSON de la respuesta
            data = response.json()
            drinks = data.get("drinks", [])

            # Modifica la estructura de datos para contener "name" y "id" como claves
            formatted_drinks = [{"name": drink["strDrink"], "id": drink["idDrink"]} for drink in drinks]

            return formatted_drinks
        except Timeout:
            print("Exceeded waiting time")
            raise TimeoutError
        except requests.exceptions.ConnectionError:
            print("No internet connection")
            raise ConnectionError
        except Exception as e:
            print("Failed to obtain drink list: " + str(e))
            raise ServerError

    def do_search_name(self, name: str) -> list:
        try:
            # Realizar una solicitud GET a la API de búsqueda
            url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + name
            response = requests.get(url, timeout=5)

            # Verificar si la solicitud fue exitosa (código de respuesta 200)
            if response.status_code == 200:
                data = response.json()

                # Verificar si la respuesta contiene datos de bebidas
                if "drinks" in data:
                    if data["drinks"] is None:
                        raise NotFoundError  # No se encontraron bebidas
                    else:
                        drinks = []

                        for drink_data in data["drinks"]:
                            drink_name = drink_data.get("strDrink", "")
                            drink_id = drink_data.get("idDrink", "")
                            drinks.append({"name": drink_name, "id": drink_id})

                        return drinks
                else:
                    raise NotFoundError # No se encontraron bebidas
            else:
                print(f"Error en la solicitud: Código {response.status_code}")
                raise ServerError  # Error en la solicitud HTTP
        except Timeout:
            print("Exceeded waiting time")
            raise TimeoutError
        except requests.exceptions.ConnectionError:
            print("No internet connection")
            raise ConnectionError
        except NotFoundError:
            print("Drink not found")
            raise NotFoundError
        except Exception as e:
            print("Failed to obtain drink list: " + str(e))
            raise ServerError


    def get_drink_details(self, id: str, language=None) -> dict:
        try:
            # Realizar una solicitud GET a la API de detalles de bebida por ID
            url = "https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=" + id
            response = requests.get(url, timeout=5)

            # Verificar si la solicitud fue exitosa (código de respuesta 200)
            if response.status_code == 200:
                data = response.json()

                print("API Response:", data)

                # Verificar si la respuesta contiene detalles de bebida
                if "drinks" in data and data["drinks"]:
                    drink_data = data["drinks"][0]  # Tomar el primer resultado

                    # Crear una lista de ingredientes y una de medidas
                    ingredients = []
                    ingredients_measures = []
                    for i in range(1, 16):
                        ingredient = drink_data.get(f"strIngredient{i}", "")
                        measure = drink_data.get(f"strMeasure{i}", "")
                        if ingredient:
                            ingredients.append(f"{ingredient}")
                            if measure is None or measure == "\n":
                                measure = ""
                            else:
                                measure = measure.strip("\n\r\t ")
                                if language is not None:
                                    measure = self.translate_measure(measure.lower(), language)
                            ingredients_measures.append(f"{measure} {ingredient}")

                    # Obtener la imagen URL
                    image_url = drink_data.get("strDrinkThumb", "")
                    drink_data["image_url"] = image_url
                    drink_data["ingredients"] = ingredients
                    drink_data["ingredientsMeasures"] = ingredients_measures

                    return drink_data
                else:
                    raise ServerError
            else:
                print("Failed to obtain ingredient details: " + str(response.status_code))
                raise ServerError
        except Timeout:
            print("Exceeded waiting time")
            raise TimeoutError
        except requests.exceptions.ConnectionError:
            print("No internet connection")
            raise ConnectionError
        except Exception as e:
            print("Failed to obtain drink list: " + str(e))
            raise ServerError

    def get_ingredient_details(self, name: str):

        try:
            #Se manda la petición para obtener los datos
            url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?i=" + name
            response = requests.get(url, timeout=5)

            if response.status_code == 200:
                #Obtenemos los datos del JSON y nos quedamos con el nombre y la descripción
                data = response.json()
                ingredient_data = data["ingredients"][0]
                ingredient_name = ingredient_data.get("strIngredient", "")
                ingredient_description = ingredient_data.get("strDescription", "")

                return {"name": ingredient_name, "description": ingredient_description}

            else:
                print("Failed to obtain ingredient details: " + str(response.status_code))
                raise ServerError

        except Timeout:
            print("Exceeded waiting time")
            raise TimeoutError
        except requests.exceptions.ConnectionError:
            print("No internet connection")
            raise ConnectionError
        except Exception as e:
            print("Failed to obtain ingredient details: " + str(e))
            raise ServerError

    def get_ingredients(self):

        try:
            # Se manda la petición para obtener los datos
            url = "https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list"
            response = requests.get(url, timeout=5)

            if response.status_code == 200:
                # Obtenemos los datos del JSON y metemos cada ingrediente en un hueco de la lista
                data = response.json()
                self.ingredients = [ingredient["strIngredient1"] for ingredient in data["drinks"]]
                return self.ingredients
            else:
                print("Error " + str(response.status_code) + " en la obtención de los ingredientes")
                raise ServerError
        except Timeout:
            print("Exceeded waiting time")
            raise TimeoutError
        except requests.exceptions.ConnectionError:
            print("No internet connection")
            raise ConnectionError
        except Exception as e:
            print("Error en la obtención de los ingredientes: " + str(e))
            raise ServerError

    def get_drinks_by_ingredient(self, ingredient):

        try:
            # Se manda la petición para obtener los datos
            url = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=" + ingredient
            response = requests.get(url, timeout=5)
            response.raise_for_status()  # Lanza una excepción si la solicitud no fue exitosa

            # Obtenemos los datos del JSON y nos quedamos con las bebias
            data = response.json()
            drinks = data.get("drinks", [])

            #Metemos en una lista las bebidas en formato diccionario con nombre y id
            formatted_drinks = [{"name": drink["strDrink"], "id": drink["idDrink"]} for drink in drinks]
            return formatted_drinks
        except Timeout:
            print("Exceeded waiting time")
            raise TimeoutError
        except requests.exceptions.ConnectionError:
            print("No internet connection")
            raise ConnectionError
        except Exception as e:
            print("Failed to obtain drink list: " + str(e))
            raise ServerError




    #Obtener la imagen
    def get_image(self, url):
        try:
            response = requests.get(url, timeout=5)
            if response.status_code == 200:  # verificamos si la solicitud HTTP fue exitosa
                image_data = response.content
                return image_data
            else:
                print(f"Error al cargar la imagen: Código de respuesta {response.status_code}")
                raise ServerError
        except Timeout:
            print("Exceeded waiting time")
            raise TimeoutError
        except requests.exceptions.ConnectionError:
            print("No internet connection")
            raise ConnectionError
        except Exception as e:
            print("Failed to obtain drink list: " + str(e))
            raise ServerError











