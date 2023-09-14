package touch.baton.fixture.vo;

import touch.baton.domain.oauth.token.Token;

public abstract class TokenFixture {

    private TokenFixture() {
    }

    public static Token token(final String value) {
        return new Token(value);
    }
}
