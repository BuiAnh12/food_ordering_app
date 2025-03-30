    package com.example.food_ordering_mobile_app.models.dish;

    public class Dish {
        String id;
        String name;
        String description;
        int price;
        String dishAvatar;
        boolean enable;

        public Dish(String id, String name, String description, int price, String dishAvatar, boolean enable) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.dishAvatar = dishAvatar;
            this.enable = enable;
        }

        public Dish(String name, String description, int price, String dishAvatar) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.dishAvatar = dishAvatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getDishAvatar() {
            return dishAvatar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean getEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public void setDishAvatar(String dishAvatar) {
            this.dishAvatar = dishAvatar;
        }
    }
