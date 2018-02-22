
package partio.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import partio.domain.EventGroup;
import partio.service.EventGroupService;

@RestController
@Transactional
public class EventGroupController {
    @Autowired
    private EventGroupService groupService;
    
    @GetMapping("/eventgroup")
    public List<EventGroup> getAll() throws IOException {
       return groupService.list();
    }
    
    //creates a new group without params since it's only made of ids
    @PostMapping("/eventgroup")
    public ResponseEntity<Object> postEventGroup() throws IOException {
       return groupService.createEventGroup();
    }
    
    //make it so it wont delete events that are completed if loggin is needed 
    //(logging is not yet needed so this will do)
    @DeleteMapping("/eventgroup/{groupId}")
    public ResponseEntity<Object> delete(@PathVariable Long groupId) throws IOException {
       return groupService.delete(groupId);
    }
    //useless line
}
