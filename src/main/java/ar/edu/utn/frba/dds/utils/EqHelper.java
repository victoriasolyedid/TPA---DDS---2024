package ar.edu.utn.frba.dds.utils;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import java.io.IOException;

public class EqHelper implements Helper<Object> {
    @Override
    public Object apply(Object first, Options options) throws IOException {
        Object second = options.param(0);
        return first != null && first.equals(second);
    }
}