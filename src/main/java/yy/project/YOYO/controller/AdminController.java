package yy.project.YOYO.controller;

import org.hibernate.service.spi.InjectService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import yy.project.YOYO.domain.Food;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.service.FoodService;
import yy.project.YOYO.service.UserService;
import yy.project.YOYO.vo.FoodVO;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@Controller
public class AdminController {

    @Inject
    FoodService foodService;

    @Inject
    UserService userService;


    @GetMapping("/admin/adminFood")
    public String adminFood(Model model){

        List<Food> allFood = new ArrayList<>();

        model.addAttribute("allFood",foodService.findAll());

        return "admin/adminFood";
    }

    @GetMapping("/admin/adminModify")
    public String adminModify(){
        return "admin/adminModify";
    }
    @GetMapping("/admin/adminAdd")
    public String adminAdd(){
        return "admin/adminAdd";
    }

    @GetMapping("/admin/adminUser")
    public String adminUser(Model model){

        List<User> Users = new ArrayList<>();

        model.addAttribute("Users", userService.findAll());

        return "admin/adminUser";
    }

    //추가
    @PostMapping("/admin/foodAdd")
    public ResponseEntity<String> foodAdd(FoodVO vo, HttpServletRequest request, Food food) {

        ResponseEntity<String> entity = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "html", Charset.forName("UTF-8")));

        int cnt = foodService.checkFood(vo.getFoodName());

        if(cnt>0){
            String msg = "<script>alert('이미 있는 음식입니다.'); history.back();</script>";

            entity = new ResponseEntity<>(msg, headers, HttpStatus.BAD_REQUEST);
        }
        else{
            //사진 파일 업로드 위한 절대 주소
            String path = request.getSession().getServletContext().getRealPath("/static/image/food");


            try{
                MultipartHttpServletRequest mr = (MultipartHttpServletRequest)  request;

                MultipartFile file = mr.getFile("filename");

                String orgFileName = file.getOriginalFilename();

                if(orgFileName != null && !orgFileName.equals("")){

                    //System.out.println("origin" + orgFileName);

                    File f = new File(path, orgFileName);

                    //중복 되는 이름이 파일이 있는 경우 숫자 추가
                    if(f.exists()){

                        for(int num=1; ; num++){

                            int idx = orgFileName.lastIndexOf(".");

                            String fileName = orgFileName.substring(0, idx);

                            String ext = orgFileName.substring(idx+1);

                            f = new File(path, fileName + "(" + num + ")." + ext);

                            if(!f.exists()){
                                orgFileName = f.getName();
                                break;
                            }
                        }//for 숫자 추가

                    }//파일 중복 확인 완료


                    try{
                        //파일 업로드
                        file.transferTo(f);
                        System.out.println("파일 업로드");
                        vo.setFoodImg(orgFileName);

                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }

                System.out.println(">>"+ vo.getFoodImg());

                food.setFoodImg(vo.getFoodImg());

                if(vo.getEvent().equals("")){
                    food.setEvent(null);
                }

                //음식 추가
                foodService.save(food);

                String msg = "<script>alert('음식이 추가가 완료되었습니다.');location.href='/admin/adminFood'; </script>";

                entity = new ResponseEntity(msg, headers,HttpStatus.OK );


            }catch (Exception e){
                e.printStackTrace();
                //파일 삭제
                deleteFile(path, vo.getFoodImg());

                String msg = "<script>alert('음식이 추가를 실패하였습니다.'); history.back(); </script>";
                entity = new ResponseEntity(msg, headers,HttpStatus.BAD_REQUEST );
            }

        }
            return entity;

    }


    //음식 종류 선택
    @PostMapping("/admin/showFoods")
    @ResponseBody
    public List<Food> showFoods(@RequestParam("foodType") String type) {

        List<Food> foods = new ArrayList<>();

        if(type.equals("전체")) {
            foods = foodService.findAll();
        }else if(type.equals("기타")){
            List<String> exclude = new ArrayList<>();
            exclude.add("한식");
            exclude.add("일식");
            exclude.add("중식");
            exclude.add("디저트");

            foods = foodService.findByFoodCategoryNotIn(exclude);
        }else {
            foods = foodService.findByFoodCategory(type);
        }
        return foods;
    }


    //검색
    @PostMapping("/admin/getFoodData")
    @ResponseBody
    public Food searchFoodData(@RequestParam("searchFood") String searchFood){
        return foodService.findByFoodName(searchFood);
    }


    //수정
    @PostMapping("/admin/foodModify")
    public ResponseEntity<String> foodModify(FoodVO vo, HttpServletRequest request, Food food){

        ResponseEntity<String> entity = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "html", Charset.forName("UTF-8")));


        Food modifyFood = foodService.findByFoodName(food.getFoodName());

        //사진 파일 이름
        String beforeFile = foodService.findByFoodName(vo.getFoodName()).getFoodImg();

        String path = request.getSession().getServletContext().getRealPath("/static/image/food");

        try{

            MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;

            MultipartFile newfile = mr.getFile("filename");

            if(newfile !=null){
                //새로운 사진 파일이 있는 경宇

                String fileName = newfile.getOriginalFilename();
                System.out.println(">>>>new " + fileName);

                if(fileName != null && !fileName.equals("")){

                    System.out.println(">>>>here " + fileName);

                    File f = new File(path, fileName);

                    if(!f.exists()){
                        //이미 같은 이름의 파일이 있는 경우
                        for(int n=1; ; n++){
                            int idx = fileName.lastIndexOf(".");
                            String fileNameExt = fileName.substring(0, idx);
                            String ext = fileName.substring(idx+1);

                            f = new File(path, fileNameExt + "(" + n + ")." + ext);

                            if(!f.exists()){
                                fileName = f.getName();
                                break;
                            }
                        }//for
                    }// if

                    //파일 넣기
                    try{
                        newfile.transferTo(f);
                        System.out.println("파일 업로드");

                        if(!newfile.isEmpty()){ //이전에 저장되었던 파일 삭제
                            deleteFile(path, beforeFile);
                        }

                        vo.setFoodImg(fileName);

                        food.setFoodImg(vo.getFoodImg());
                        System.out.println(food.getFoodImg());

                    }catch (Exception ex){

                    }
                }//if(fileName != null && fileName.equals(""))
            }//if(newfile !=null)

            if(vo.getEvent().equals("")){
                vo.setEvent(null);
            }

            modifyFood.setFoodCategory(vo.getFoodCategory());
            modifyFood.setEvent(vo.getEvent());
            modifyFood.setPriority(vo.getPriority());
            modifyFood.setSeason(vo.getSeason());
            modifyFood.setTemperature(vo.getTemperature());
            modifyFood.setWeather(vo.getWeather());

            if(vo.getFoodImg() != null && !vo.getFoodImg().equals("")){
                modifyFood.setFoodImg(vo.getFoodImg());
            }
            foodService.save(modifyFood);

            String msg = "<script>alert('음식 수정이 완료되었습니다.');location.href='/admin/adminFood';</script>";

            entity = new ResponseEntity(msg, headers, HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();

            deleteFile(path, vo.getFoodImg());

            String msg = "<script>alert('음식 수정을 실패하였습니다.');history.back();</script>";
            entity = new ResponseEntity(msg, headers, HttpStatus.BAD_REQUEST);

        }
        return entity;
    }

    //회원 페이지
    


    //파일 삭제
    public void deleteFile(String p, String f) {

        if(f!=null) {

            File file = new File(p,f);
            file.delete();
        }

    }


}
