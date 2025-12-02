package com.example.ordersystem.until;

import com.example.ordersystem.bean.FoodBean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CartManager {
    private static Map<String, Integer> cartMap = new HashMap<>();
    private static Map<String, FoodBean> foodMap = new HashMap<>();

    public static void clear() {
        cartMap.clear();
        foodMap.clear();
    }

    public static void add(FoodBean food) {
        int count = cartMap.getOrDefault(food.getFoodID(), 0);
        cartMap.put(food.getFoodID(), count + 1);
        foodMap.put(food.getFoodID(), food);
    }

    public static void reduce(FoodBean food) {
        String id = food.getFoodID();
        if (cartMap.containsKey(id)) {
            int count = cartMap.get(id);
            if (count > 1) {
                cartMap.put(id, count - 1);
            } else {
                cartMap.remove(id);
                foodMap.remove(id);
            }
        }
    }

    public static int getFoodCount(String foodId) {
        return cartMap.getOrDefault(foodId, 0);
    }

    public static int getTotalCount() {
        int total = 0;
        for (Integer count : cartMap.values()) {
            total += count;
        }
        return total;
    }

    public static double getTotalPrice() {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : cartMap.entrySet()) {
            String id = entry.getKey();
            int count = entry.getValue();
            FoodBean food = foodMap.get(id);
            if (food != null) {
                try {
                    double price = Double.parseDouble(food.getFoodPrice());
                    total += price * count;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return total;
    }

    public static Map<FoodBean, Integer> getCartItems() {
        Map<FoodBean, Integer> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : cartMap.entrySet()) {
            result.put(foodMap.get(entry.getKey()), entry.getValue());
        }
        return result;
    }
}