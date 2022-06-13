package yy.project.YOYO.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yy.project.YOYO.domain.Food;
import yy.project.YOYO.repository.FoodRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService{

    private final FoodRepository foodRepository;


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

    @Override
    public List<Food> findByEvent(String event) {
        return foodRepository.findByEvent(event);
    }

    @Override
    public List<Food> findByWeather(String weather) {
        return foodRepository.findByWeather(weather);
    }

    @Override
    public List<Food> findBySeason(String season) {
        return foodRepository.findBySeason(season);
    }

    @Override
    public List<Food> findByTemperature(String temperature) {
        return foodRepository.findByTemperature(temperature);
    }

    @Override
    public List<Food> findByPriority(Character priority) {
        return foodRepository.findByPriority(priority);
    }

    @Override
    public Food findByfID(Long fID) {
        return foodRepository.findByfID(fID);
    }


}
