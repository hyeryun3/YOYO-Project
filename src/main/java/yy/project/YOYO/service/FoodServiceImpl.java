package yy.project.YOYO.service;

import org.springframework.stereotype.Service;
import yy.project.YOYO.domain.Food;
import yy.project.YOYO.repository.FoodRepository;
import yy.project.YOYO.vo.FoodVO;

import java.util.ArrayList;
import java.util.List;

@Service
public class FoodServiceImpl implements FoodService{

    private final FoodRepository foodRepository;

    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public int checkFood(String foodName) {
        return foodRepository.checkFood(foodName);
    }

    @Override
    public Food save(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public List<Food> findByFoodCategory(String foodType) {
        return foodRepository.findByFoodCategory(foodType);
    }

    @Override
    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    @Override
    public List<Food> findByFoodCategoryNotIn(List<String> foods) {

        return foodRepository.findByFoodCategoryNotIn(foods);
    }

    @Override
    public Food findByFoodName(String foodName) {
        return foodRepository.findByFoodName(foodName);
    }


}
