from view import View


from model import Model
from presenter import Presenter
import locale
import gettext

if __name__ == '__main__':
    locale.setlocale(locale.LC_ALL, '')
    LOCALE_DIR = "./locales"
    locale.bindtextdomain('SearchCocktails', LOCALE_DIR)
    gettext.bindtextdomain('SearchCocktails', LOCALE_DIR)
    gettext.textdomain('SearchCocktails')
    presenter = Presenter(model=Model(), view=View())
    presenter.run(application_id="es.udc.fic.ipm.SearchCocktails")
