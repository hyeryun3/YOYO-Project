package yy.project.YOYO.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import yy.project.YOYO.domain.User;
import yy.project.YOYO.repository.UserRepository;
import yy.project.YOYO.vo.UserVO;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    public int memberCheck(String userID){
        User user = userRepository.findByUserID(userID);

        if(userID==""){
            return -1;
        }else if(user==null){
            return 0;
        }else{
            return 1;
        }
    }

    public User findByUserID(String userID){
        return userRepository.findByUserID(userID);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public String filePathForUserProfileImage(List<MultipartFile> images) throws Exception{

        String userPhoto = null;

        if(!CollectionUtils.isEmpty(images)){ //이미지 파일이 존재할 경우
            //프로젝트 내의 static 폴더까지의 절대 경로
            String absolutePath = new File("").getAbsolutePath()+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static";

            for(MultipartFile image : images){
                String originalFileExtension = new String();
                String contentType = image.getContentType();

                if(ObjectUtils.isEmpty(contentType)){ //확장자가 없는 파일 -> 처리 x
                    break;
                }
                else{
                    if(contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else  // 다른 확장자일 경우 처리 x
                        break;
                }

                String newFileName = System.nanoTime()+originalFileExtension; //이미지 이름이 겹치지 않게 나노시간을 이름으로 사진 저장
                File file = new File(absolutePath+File.separator+"userProfileImage"+File.separator+newFileName);
                image.transferTo(file);
                file.setWritable(true);
                file.setReadable(true);

                userPhoto = File.separator + "userProfileImage" + File.separator + newFileName;

            }
        }
        return userPhoto;
    }

    public void updateUser(String userID, UserVO form) throws Exception {

        String userImage = filePathForUserProfileImage(form.getUserImage());

        User user = findByUserID("rabbith3");
        user.setUserID(form.getUserID());
        user.setPassword(form.getPassword());
        user.setUserName(form.getUserName());
        user.setEmail(form.getEmail());
        user.setAddress(form.getAddress());

        if(userImage != null){
            user.setUserImage(userImage);
        }

        userRepository.save(user);

    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }


    //아이디,비밀번호 찾기
    @Override
    public List<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUserNameAndEmail(String userName,String email){ return userRepository.findByUserNameAndEmail(userName,email); }

    @Override
    public String getTempPW(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    @Override
    @Async
    public void mailToPW(String userName,String email, String tempPW){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);                              //수신자
        message.setFrom("mannayo0609@gmail.com");        //발신자.
        message.setSubject("[YOYO!] "+userName+"님 임시비밀번호 발급 메일입니다.");       //메일 제목
        String sendMsg = userName+"님의 임시 비밀번호 생성 안내를 위해 발송된 메일입니다.\n 임시 비밀번호는 "+tempPW + "입니다.";
        message.setText(sendMsg);                   //메일 내용
        javaMailSender.send(message);
    }

}
