package com.driver;

import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<String,User> userDB;

    private HashMap<Integer,Message> messageDB;
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;



    public WhatsappRepository(){
        this.userDB = new HashMap<String,User>();
        this.messageDB = new HashMap<Integer,Message>();
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }



    public int getCustomGroupCount() {
        return customGroupCount;
    }

    public void setCustomGroupCount(int customGroupCount) {
        this.customGroupCount = customGroupCount;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }


    public String createUser(String name, String mobile) throws Exception {
        User user = new User(name,mobile);
        if(!this.userDB.isEmpty() && this.userDB.containsKey(mobile)){
            throw new Exception("User already exists");
        }
        this.userDB.put(mobile,user);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        for(User user : users){
            if(!userDB.containsKey(user.getMobile())){
                userDB.put(user.getMobile(),user);
            }
        }
        Group group = null;
        int groupSize = users.size();
        if(groupSize == 2){
            group = new Group(users.get(1).getName(),2);
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
        }
        else if(groupSize > 2){
            customGroupCount++;
            String groupName = "Group "+(customGroupCount);
            group = new Group(groupName,groupSize);
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
        }
        return group;
    }

    public int createMessage(String content) {
        messageId++;
        Message message = new Message(messageId,content);
        messageDB.put(messageId,message);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.
        if(groupUserMap.containsKey(group) == false){
            throw new Exception("Group does not exist");
        }
        List <User> userList = groupUserMap.get(group);
        if(userList.contains(sender) == false){
            throw new Exception("You are not allowed to send message");
        }
        List<Message>messageList = groupMessageMap.get(group);
        if(messageList == null){
            messageList = new ArrayList<>();
        }
        messageList.add(message);
        groupMessageMap.put(group, messageList);
        senderMap.put(message,sender);
        return messageList.size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.
        if(groupUserMap.containsKey(group) == false){
            throw new Exception("Group does not exist");
        }
        if(adminMap.get(group) != approver){
            throw new Exception("Approver does not have rights");
        }
        List <User> userList = groupUserMap.get(group);
        if(userList.contains(user) == false){
            throw new Exception("User is not a participant");
        }
        adminMap.put(group,user);
        return "SUCCESS";
    }

}
