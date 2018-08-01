package don.geronimo.testejwt.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFilter extends GenericFilterBean {
    @Value("${sistema.secret}")
    private String jwtSecret;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("HELLO");
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type");
        response.setHeader("Access-Control-Expose-Headers", "X-File-Name");

        //pular se for request de preflight
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            retornar(request, response, HttpServletResponse.SC_FORBIDDEN, "Token nao encontrado");
            return;
        }

        final String token = authHeader.substring(7); //Após "Bearer "

        try {
            final Claims claims = Jwts.parser().setSigningKey(jwtSecret)
                    .parseClaimsJws(token).getBody();

            //TODO negar acesso caso token esteja expirado

            String idusuarioStr = claims.get("iduser").toString();

            request.setAttribute("iduser", idusuarioStr);

            System.out.println("Usuario injetado na request com sucesso");

            filterChain.doFilter(request, response);

        } catch (final SignatureException e) {

            retornar(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Token invalido");

        }
    }

    private void retornar(HttpServletRequest request, HttpServletResponse response, int status, String msg) throws IOException {
        response.reset();
        response.setStatus(status);
        response.getWriter().write(msg);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
