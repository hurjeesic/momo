package team.ohjj.momo.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.ohjj.momo.domain.ChatRoom;
import team.ohjj.momo.entity.ChatRoomJpaRepository;
import team.ohjj.momo.entity.ChatRoomRepository;
import team.ohjj.momo.entity.ProjectJpaRepository;

import java.util.List;

@RestController
@RequestMapping(value = "/api/chat/room")
public class ChatRoomRestController {
    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ProjectJpaRepository projectJpaRepository;

    @GetMapping("/{id}")
    public ChatRoom getChatRoom(@PathVariable String id) {
        return chatRoomJpaRepository.findById(id).get();
    }

    @GetMapping("/list")
    public List<ChatRoom> getChatRoomListByProject(@RequestParam Integer projectId) {
        return chatRoomJpaRepository.findAllByProject(projectJpaRepository.findById(projectId).get());
    }

    @PutMapping("/insert")
    public ChatRoom createChatRoom(@RequestParam String chatRoomName, @RequestParam Integer projectId) {
        return chatRoomRepository.createChatRoom(chatRoomName, projectId);
    }

    @PutMapping("/update/{id}")
    public void updateChatRoom(@PathVariable String id, @RequestParam String name) {
        ChatRoom chatRoom = chatRoomJpaRepository.findById(id).get();

        chatRoom.setName(name);

        chatRoomJpaRepository.save(chatRoom);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteChatRoom(@PathVariable String id) {
        chatRoomJpaRepository.deleteById(id);
    }
}
