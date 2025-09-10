//package techtrek.domain.analysis.service.bean;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import techtrek.domain.analysis.dto.AnalysisResponse;
//import techtrek.domain.analysis.service.small.CreateSessionListDTO;
//import techtrek.domain.Interview.entity.SessionInfo;
//import techtrek.domain.interviewQuestion.entity.status.EnterpriseName;
//import techtrek.domain.Interview.service.small.GetSessionInfoListDAO;
//import techtrek.domain.user.entity.User;
//import techtrek.domain.user.service.small.GetUserDAO;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class GetAnalysisListBean {
//    private final GetUserDAO getUserDAO;
//    private final GetSessionInfoListDAO getSessionInfoListDAO;
//    private final CreateSessionListDTO createSessionListDTO;
//
//    // 세션 리스트 불러오기
//    public AnalysisResponse.SessionList exec(EnterpriseName enterpriseName){
//        // TODO:사용자 조회
//        User user = getUserDAO.exec("1");
//
//        // 해당 기업의 세션정보 list 조회 (내림차순)
//        List<SessionInfo> sessionInfos = getSessionInfoListDAO.exec(user.getId(), enterpriseName);
//
//        return createSessionListDTO.exec(sessionInfos);
//    }
//}
