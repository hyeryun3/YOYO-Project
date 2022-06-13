package yy.project.YOYO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yy.project.YOYO.domain.Food;
import yy.project.YOYO.vo.FoodVO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query(value="select count(f) from Food f where f.foodName=:foodName")
    int checkFood(@Param("foodName") String foodName);

    List<Food> findByFoodCategory(@Param("foodType") String foodType);

    List<Food> findAll();

    List<Food> findByFoodCategoryNotIn(List<String> food_type);

    Food findByFoodName(@Param("foodName") String foodName);

    List<Food> findByEvent(String event);

    List<Food> findByWeather(String weather);

    List<Food> findBySeason(String season);

    List<Food> findByTemperature(String temperature);

    List<Food> findByPriority(Character priority);

    Food findByfID(@Param("fID") Long fID);


}
