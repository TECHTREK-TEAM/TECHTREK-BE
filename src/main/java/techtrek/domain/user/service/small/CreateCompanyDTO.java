package techtrek.domain.user.service.small;

import org.springframework.stereotype.Component;
import techtrek.domain.user.dto.UserResponse;

import java.util.List;

@Component
public class CreateCompanyDTO {

    // 기업 to3 dto
    public UserResponse.CompanyList exec(List<UserResponse.CompanyList.Company> top3Companies) {
        return new UserResponse.CompanyList(top3Companies);
    }
}
