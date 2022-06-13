package yy.project.YOYO.service;

import org.springframework.stereotype.Service;
import yy.project.YOYO.domain.Food;

import java.util.List;

@Service
public interface FoodService {

    //추가하기 전 해당 이름의 음식 확인
    int checkFood(String foodName);

    //음식 추가
    Food save(Food food);

    //해당 종류의 음식 가져오기
    List<Food> findByFoodCategory(String foodType);

    //전체 음식
    List<Food> findAll();

    //기타 음식 가져오기
    List<Food> findByFoodCategoryNotIn(List<String> foods);

    //검색한 음식 1개 가져오기
    Food findByFoodName(String foodName);

    List<Food> findByEvent(String event);

    List<Food> findByWeather(String weather);

    List<Food> findBySeason(String season);

    List<Food> findByTemperature(String temperature);

    List<Food> findByPriority(Character priority);

    Food findByfID(Long fID);


}
