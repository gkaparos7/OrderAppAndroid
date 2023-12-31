# OrderAppAndroid ![Screenshot_20231218_193428_OrderAppAndroid (2)](https://github.com/gkaparos7/OrderAppAndroid/assets/122390421/a97bfe7c-0906-4ef3-a5db-d87796059289)
Welcome to OrderAppAndroid, a powerful and efficient mobile application designed to streamline the inventory management process, specifically tailored for business owners and individuals involved in product ordering. 

OrderAppAndroid is an Android studio frontend app to be combined with the OrderAppSpringBoot.

## Introduction
The genesis of OrderAppAndroid emanated from the practical challenges of my retail shop and the lack of digitalization of my supplier ordering systems. This application addresses a ubiquitous issue encountered by shop owners – the cumbersome process of manually compiling and organizing product orders.

## Purpose
OrderAppAndroid endeavors to simplify and expedite the order creation process by providing a user-friendly platform for managing wishlists, orders, and historical records. Born out of a genuine necessity, this application caters to the needs of business owners who seek efficiency in managing their inventory and placing orders with suppliers.

## Key Features
### -User Authentication: 
Securely register and log in to the app, ensuring data privacy and user-specific functionality.

### -Wishlist Management: 
Create and manage wishlists to keep track of products needed for future orders.

### -Order Creation: 
Seamlessly transition from wishlists to orders, streamlining the process of generating and sending orders to suppliers.

### -Order History:
Access a comprehensive record of previous orders, facilitating easy reference and reordering.

## Getting Started
Before using the OrderAppAndroid, ensure that the OrderAppSpringBoot backend is up and running. Follow these steps to configure the app:

1. Find out the IP address of your computer using `ipconfig`.
2. Open the Android app source code and locate URLs in the following files:
   - `LoginActivity.java`
   - `RegisterActivity.java`
   - Files in the `requests` folder

Update the base URLs in these files with your computer's IP address to ensure proper communication with the backend.

The OrderAppSpringBoot has already some data to be used if needed. For example "admin@admin.gr" for username and "12345" can work for logging in, and finding some wishlist items and orders ready. The category Judo is correctly populated with subcategories and products.

## Inside the OrderApp
In the beggining the user will have to login in order to be moved to the main menu, these 2 screens is what the user will see when opening hte app.

![Screenshot_20231218_193428_OrderAppAndroid](https://github.com/gkaparos7/OrderAppAndroid/assets/122390421/0a4f2a76-7a2b-4343-80bd-e0a7afd67ccf)
![Screenshot_20231218_193434_OrderAppAndroid](https://github.com/gkaparos7/OrderAppAndroid/assets/122390421/e4f5c6e8-30a7-4b1c-a077-f0d75abaebf4)

After loging in the home fragment welcomes the user and gives 3 options to choose from.

![Screenshot_20231218_193452_OrderAppAndroid](https://github.com/gkaparos7/OrderAppAndroid/assets/122390421/91603dab-44ad-49bd-9140-f50681f4ae37)

When the Products Fragment is chosen the first thing the user sees are the categories of the products.

![Screenshot_20231218_193501_OrderAppAndroid](https://github.com/gkaparos7/OrderAppAndroid/assets/122390421/2f41fd59-632a-4e4e-b6e8-c4bda8a9626c)

The user chooses a category, for example in this scenario "Judo". And all of the subcategories of this category appears.

![Screenshot_20231218_193504_OrderAppAndroid](https://github.com/gkaparos7/OrderAppAndroid/assets/122390421/f6f031e1-fcf6-44d3-bdeb-8edce1fb84e3)

Finally when a subcategory is chosen it leads to the products in this subcategory.

![Screenshot_20231218_193510_OrderAppAndroid](https://github.com/gkaparos7/OrderAppAndroid/assets/122390421/cd7bf1b2-90a0-43a8-a887-cfdeddadc648)

In this section the user can choose a product, and the product view is enlarged and some more option appear.

The quantity editbox to insert the wanted quantity of the product in the chosen size.

The dropdown menu to choose on of the available sizes.

The button "Add to wishlist" to do exactly what it says.

And the close button on the top right corner of the product, to minmize the product view if you change your mind.

If you add a product to the wishlist, the view minimizes and a toast message for succesion appears.

![Screenshot_20231218_193755_OrderAppAndroid](https://github.com/gkaparos7/OrderAppAndroid/assets/122390421/fe65480b-3991-4885-86b9-a4b22ab6cf00)

When the Wishlist Fragment is chosen the user can see the wishlist that is linked to him/her.

There is only one wishlist to every user.

The user can clear the whole wishlist pressing the according button on top right corner.

An order from the wishlist can be easily made using the top left corner. The order is created, the wishlist empties and a toast message for succesion is appeared.

If the user wants to delete an item from the wishlist can easily do it by swiping left.

![Screenshot_20231218_232051_OrderAppAndroid](https://github.com/gkaparos7/OrderAppAndroid/assets/122390421/003986fe-9d4c-4c9f-80ac-b59f3ef1c62d)

When the Orders Fragment is chosen the user can see all of his/her orders, latest order first.

![Screenshot_20231220_225145_OrderAppAndroid](https://github.com/gkaparos7/OrderAppAndroid/assets/122390421/010b96a2-e4c3-4d04-9012-24eea0704d69)

It is possible to choose an order and see all the order items that are in it.

The back button takes you from the order items list to the orders view.



