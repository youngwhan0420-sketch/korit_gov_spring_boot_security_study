package com_korit.security_study.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Integer userId;
    private String username;
    @JsonIgnore //이걸 걸어주면 password는 제외하고 postman에서 json으로 응답이 된다.
    private String password;
    private String email;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    // 객체 지향적 설계름 위함 -> 객체를 참조할 수 있는 구조를 선호
    private List<UserRole> userRoles;

}

// 권한 목록과 유저 권한 목록을 따로 둔 이유
// 만약에 유저당 하나의 권한만 가질 수 있다면
// User는 하나의 role만 가질 수 있음
// Role은 N명의 사용자에게 부여될 수 있음
// 1:N 관계
// 하지만, 관리자이면서 일반사용자인 경우 동시에 두개의 권한을 가질 수 없는 구조

// User가 여러 권한을 가질 수 있을 때
// Role도 여러 User에게 부여될 수 있고
// -> N:M 관계
// 이러한 권한 관리가 복잡해지기 때문에 권한 목록인 중간 테이블을 따로 분리해서 관리