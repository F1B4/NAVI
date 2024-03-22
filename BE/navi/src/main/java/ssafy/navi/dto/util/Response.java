package ssafy.navi.dto.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class Response<D> {

    private final String resultCode;
    private final String message;
    private final D data;
}
