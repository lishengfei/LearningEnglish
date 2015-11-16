package carloslobo.com.finalproject.Modules.Interfaces;

import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by camilo on 11/11/15.
 */
public interface AsyncResponse  extends  Async{
    void Query() throws ParseException;
    void ProcessQuery(ParseObject JSON) throws ParseException;
}
