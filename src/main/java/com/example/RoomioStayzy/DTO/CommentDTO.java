package com.example.RoomioStayzy.DTO;

public class CommentDTO {
    private String comment;
    private Long house_Id;

    public CommentDTO(Long house_Id, String comment) {
        this.house_Id = house_Id;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getHouse_Id() {
        return house_Id;
    }

    public void setHouse_Id(Long house_Id) {
        this.house_Id = house_Id;
    }
}
