package br.fiap.hackathonpostech.infra.mapper;

import br.fiap.hackathonpostech.domain.vo.LoginVO;

public class LoginMapper {
    private LoginMapper() {
    }

    public static LoginVO stringToLoginVO(String token) {
        return new LoginVO(token);
    }
}
