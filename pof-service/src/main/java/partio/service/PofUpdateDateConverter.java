
package partio.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class PofUpdateDateConverter {
    
    public static LocalDateTime getDateFromPof(ObjectNode pof) {
        if (pof == null) {
            return LocalDateTime.MIN;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String lastTimeJson = pof.findValue("program").
                findValue("treeDetails").
                findValue("lastModified").asText();
        LocalDateTime lastTime = LocalDateTime.parse(lastTimeJson, formatter);
        return lastTime;
    }
}
