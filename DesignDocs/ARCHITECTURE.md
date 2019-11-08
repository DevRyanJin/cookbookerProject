**3-Tier Architecture**
---
**Here is an explination of the function of each class and where it sits in the directory**
 * **Presentation Layer**
   * DailyRecipeActivity -> An overlay that gets the recipe of the day from the DB and allows the user to view it.
   * EditRecipeActivity -> Interface for users to create new recipes based off existing ones.
   * MainActivity -> Main screen that is displayed when the app is launched (contains settings, recipes, search buttons), Also handles display of search results.
   * Message -> Popup to inform users of various issues/warnings/errors
   * RecipeActivity -> Standardized display for a single Recipe and all aspects associated with.
   * RecipeAdapter -> An adapter to force the Recipe object to conform to a display within a list of recipes.
   * ShoppingListActivity -> Allows users to create a custom shopping list and search for recipes containing shopping list items.
   * ShoppingListAdapter ->An adapter used to show the model items in a list
   * ShoppingListItemModel -> A small wrapper class to handle items in the shopping list. 
 * **Business/Logic Layer**
   * Folder:exceptions -> A complte listing of the included exceptions that may be thrown
   * Service -> A main service which instanciates and allows for general communication with a single instanced database via AccessRecipe.
   * AccessRecipe -> A general handeler which allows communication between the persistance layer and the Recipe objects from the data layer.
   * RecipeValidator -> Validates that the Recipe objects conform to a standard when changes occurr.
 * **Persistence/Data Layer**
   * Database -> An interface which defines the communications to and fro the implemented database.
   * Database_HSQL -> A database interfaced with SQL 
   * Base64 -> A backported class for functionality which enables storage of pictures as strings in the database.
   * ~~Stub_Database -> A strewn together database implementation for early testing.~~
 * **Domain Model**
   * Ingredient -> The Object which Contains a ID, Susbstence(Name), Units, and an Amount. To be tied to a specific Recipe Object.
   * Picture -> The Object which contains am ID and a byte array holding the binary data of the picture.
   * Recipe -> The Object which contains an ID, Title, Directions, Ingredients, Tags, and a Gallery of Photos
   * ~~Tag -> Currently unimplemented`The Object which Defines a specific Tag used in one or more recipes. Contains a Name and ID`~~

**The general workflow will will be as follows**
 * User opens app to MainActivity(Populate with ~~random~~ all Recipes)
   * User opens the Search function, enters criteria which relaunches MainActivity(given Search Results)
   * User opens the Settings tab and selects one item from the list (opens other Activities if required)
     * `Find A Random Recipe!` Selects a random recipe from the DB and allows the user to view it.
     * `Search` Allows the user to search for specific recipes
     * `Shopping List` Allows the user to create a shopping list, which can have select items queried against the DB
     * `New Recipe` Allows user to create a whole new recipe from scratch
   * User Scrolls to and clicks on a recipe they wish to view
     * RecipeView(clickedRecipeID) is opened so the user can view all aspects of the recipe
       * This is also where the user may change or update any portion of the recipe

**Our rough draft GUIs with general workflow were sketched up as follows:**

![General Worflow & Early GUI](https://i.imgur.com/sN3vvLG.jpg)


**The General DomainModel can be derived from the following RelationalModel sketch**

![Relational Model Sketch](https://i.imgur.com/3kSn7tR.jpg)
