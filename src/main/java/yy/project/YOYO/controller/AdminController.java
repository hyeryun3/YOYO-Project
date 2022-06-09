package yy.project.YOYO.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import yy.project.YOYO.domain.Food;
import yy.project.YOYO.domain.Team;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.service.FoodService;
import yy.project.YOYO.service.TeamService;
import yy.project.YOYO.service.UserService;
import yy.project.YOYO.vo.FoodVO;
import yy.project.YOYO.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.inject.Inject;

@Controller
public class AdminController {

    @Inject
    FoodService foodService;

    @Inject
    UserService userService;

    @Inject
    TeamService teamService;


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
    public String adminUser(Model model, @PageableDefault( page = 0, size = 10, sort = "uID", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(required = false, defaultValue = "") String searchWord){

        Page<User> list = null;
        String searchWord1 = searchWord;
        String searchWord2 = searchWord;
        String searchWord3 = searchWord;
        String searchWord4 = searchWord;


        if(searchWord == null){
            list = userService.findAll(pageable);

        }else{
            list = userService.findByUserIDIgnoreCaseContainingOrAddressContainingOrUserNameIgnoreCaseContainingOrEmailIgnoreCaseContaining(searchWord1, searchWord2, searchWord3, searchWord4, pageable);
        }


        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage -5, 1);
        int endPage = Math.min(nowPage + 4, list.getTotalPages());


        model.addAttribute("Users", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("searchWord", searchWord);
        model.addAttribute("totalPage", list.getTotalPages());



        return "admin/adminUser";
    }

    //회원 탈퇴
    @PostMapping("/admin/withdrawUser")
    public String withdrawUser(UserVO vo, Model model){

        //System.out.println(vo.getUserList().size());

        List<String> list = new ArrayList<>();

        /*for(String user : vo.getUserList()){
            System.out.println(user + " ");
        }*/

        userService.deleteByUserIDIn(vo.getUserList());

        return "redirect:/admin/adminUser";
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


            String path = new File("").getAbsolutePath()+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static" + File.separator+"adminImage" + File.separator+"food";



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
    @PostMapping("/showFoods")
    @ResponseBody
    public List<Food> showFoods(@RequestParam("foodType") String type) {

        List<Food> food = foodList(type);

        System.out.println("hello");

        return food;
    }


    //검색
    @PostMapping("/admin/getFoodData")
    @ResponseBody
    public Food searchFoodData(@RequestParam("searchFood") String searchFood){
        return foodService.findByFoodName(searchFood);
    }

    @PostMapping("/selectFoodOk")
    @ResponseBody
    public String selectFoodOk(@RequestParam("fID") Long fID, @RequestParam("tID") Long tID){

        Team team = teamService.findBytID(tID);

        Food food = foodService.findByfID(fID);
        System.out.println(food.getFoodName());

        System.out.println(team.getTeamName());

        team.setFood(food);

        teamService.save(team);

        return "OK";
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

        String path = new File("").getAbsolutePath()+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static" + File.separator+"adminImage" + File.separator+"food";

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

                    if(f.exists()){
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
                }//if
            }//if

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

    @RequestMapping(value="/getFoodRecommend", method=RequestMethod.GET)
    @ResponseBody
    public List<Food> foodRecommend(String weather, String temperature){

        System.out.println(weather);
        System.out.println(temperature);
        //음식 추천
        //우선 순위 있는 음식에서 2개

        //음식 추천 화면으로 보낼 음식 리스트
        List<Food> list = new ArrayList<Food>();

        //날씨 일치, 계절 일치, 온도 해당 하는 음식 저장할 리스트
        ///////////////////////////////////////////
        HashSet<Food> foods = new HashSet<Food>();


        int cnt=0;
        //cnt<=2까지 일때
        //1. 오늘 날짜와 일치하는 이벤트 날짜 있는지 확인 -- 0개, 1개, 2개 이상...(여러개 있다면 이중 1개 선택해서 출력 리스트에 추가)
        //오늘 날짜
        LocalDate now = LocalDate.now();
        System.out.println(now.toString());
        //오늘 날짜와 일치하는 이벤트 있는 음식 저장
        List<Food> event = foodService.findByEvent(now.toString());


        if(event.size()>=2) {
            //1개 선택하기
            Collections.shuffle(event);
            list.add(event.get(0));
            cnt=1;
        }else if(event.size()==1) {
            //1개
            list.add(event.get(0));
            cnt=1;
        }
        //0개인 경우 - cnt = 0 넘어간다


        //2. 오늘 날씨와 일치하는 음식 있는지 확인
        //오늘 날씨
        //System.out.println(weather);

        String todayWeather = "0";

        if(weather.contains("맑음")) {
            todayWeather = "clear";

        }else if(weather.contains("비") || weather.contains("소나기")) {

            todayWeather = "rain";

        }else if(weather.contains("눈")) {
            todayWeather = "snow";

        }
        if(!todayWeather.equals("0")) {
            foods.addAll(foodService.findByWeather(todayWeather));
        }


        //3. 오늘 계절과 일치하는 음식 있는지 확인
        int month = now.getMonthValue();

        String season = "0";

        if(month>=3 && month <=5) {
            season = "spring";
        }else if(month>=6 && month <=8) {
            season = "summer";
        }else if(month>=9 && month <=11) {
            season = "fall";
        }else if(month <= 2 || month ==12) {
            season = "winter";
        }

        if(!season.equals("0")) {
            foods.addAll(foodService.findBySeason(season));
        }


        //4. 오늘 온도에 해당하는 음식 있는지 확인
        Double tem = Double.parseDouble(temperature);
        String temp = "0";

        if(tem <=15) {
            temp = "2";
        }else if(tem>=25) {
            temp= "1";
        }

        if(!temp.equals("0")) {
            foods.addAll(foodService.findByTemperature(temp));
        }



        //event에서 선택된 것과 중복되는 데이터 삭제
        if(list.size()>0) {
            foods.removeIf(FoodVO->FoodVO.getFoodName().equals(list.get(0).getFoodName()));
        }

        // 2,3,4추가후 객체 중복 제거
        //fname으로 객체 제거

        for(Food fvo: foods) {
            System.out.println(fvo.getFoodName());
        }

        List<Food> f = new ArrayList<Food>(foods);

        //2,3,4, 담는 리스트에 추가 <- (1번이 0개이면 2개 추출, 1개 이상이면 1개 추출)


        if(f.size()>0) {

            Collections.shuffle(f);
            list.add(f.get(0));
            cnt++;

            if(cnt==1 && f.size()>1) {
                list.add(f.get(1));
                cnt++;
            }
        }

        for(Food fvo: list) {
            System.out.println(fvo.getFoodName()+ "+");
            System.out.println(fvo.getFID() + " ");
        }


        //우선 순위 없는 음식 가져오기
        //priorty==N인 음식 리스트 가져오기 그중 무작위 3개 또는 4개 또는 5개
        List<Food> priorityN = foodService.findByPriority('N');
        Collections.shuffle(priorityN);

        int i=0;
        while(cnt<5) {
            list.add(priorityN.get(i));
            i++;
            cnt++;
        }


		for(Food fvo: list) {
			System.out.println(fvo.getFID());
		}


        return list;

    }


    //음식 정보 함수
    public List<Food> foodList(String type) {

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





    //파일 삭제
    public void deleteFile(String p, String f) {

        if(f!=null) {

            File file = new File(p,f);
            file.delete();
        }

    }

    @GetMapping("/admin/foodRecommend")
    public String test(){
        return "admin/foodRecommend";
    }

    @PostMapping("/teamFoodName")
    @ResponseBody
    public String teamFoodName(@RequestParam("tID") Long tID){

        Team team = teamService.findBytID(tID);

        Food food = foodService.findByfID(team.getFood().getFID());

        String foodName = food.getFoodName();

        System.out.println(foodName);

        System.out.println(team.getTeamName());

        return foodName;
    }

    @PostMapping("/getLocationX")
    @ResponseBody
    public String getLocationX(@RequestParam("tID") Long tID){

        Team t = teamService.findBytID(tID);

        System.out.println(t.getPlaceX());

        return t.getPlaceX();
    }

    @PostMapping("/getLocationY")
    @ResponseBody
    public String getLocationY(@RequestParam("tID") Long tID){

        Team t = teamService.findBytID(tID);

        System.out.println(t.getPlaceY());

        return t.getPlaceY();
    }



}
