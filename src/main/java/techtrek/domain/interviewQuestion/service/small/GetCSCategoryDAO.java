//package techtrek.domain.basicQuestion.service.small;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import techtrek.domain.basicQuestion.entity.status.Category;
//import techtrek.global.common.code.ErrorCode;
//import techtrek.global.common.exception.CustomException;
//
//@Component
//@RequiredArgsConstructor
//public class GetCSCategoryDAO {
//
//    // cs 조회
//    public Category exec(String selectedKeyword) {
//
//        return Category.fromKeyword(selectedKeyword)
//                .orElseThrow(() -> new CustomException(ErrorCode.ENUM_CS_KEYWORD_NOT_FOUND));
//    }
//}
