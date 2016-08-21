package ykt.ios4miui3.gavrique.models.Telegram;

import java.util.List;

/**
 * Created by Silent on 21.08.2016.
 */
public class Message {
    public enum Type {
        STICKER, TEXT, AUDIO, VOICE, DOCUMENT, PHOTO, LOCATION, CONTACT
    }

    private long message_id;
    private User from;
    private long date;
    private Chat chat;
    private User forward_from;
    private Chat forward_from_chat;
    private long forward_date;
    private Message reply_to_message;
    private long edit_date;
    private String text;
    private List<MessageEntity> entities;
    private Audio audio;
    private Document document;
    private List<PhotoSize> photo;
    private Sticker sticker;
    private Video video;
    private Voice voice;
    private String caption;
    private Contact contact;
    private Location location;
    private Venue venue;
    private User new_chat_member;
    private User left_chat_member;
    private String new_chat_title;
    private List<PhotoSize> new_chat_photo;
    private boolean delete_chat_photo = true;
    private boolean group_chat_created = true;
    private boolean supergroup_chat_created = true;
    private boolean channel_chat_created = true;
    private int migrate_to_chat_id;
    private int migrate_from_chat_id;
    private Message pinned_message;

    public Type getType() {
        if (this.getAudio() != null) {
            return Type.AUDIO;
        }

        if (this.getSticker() != null) {
            return Type.STICKER;
        }

        if (this.getVoice() != null) {
            return Type.VOICE;
        }

        if (this.getDocument() != null) {
            return Type.DOCUMENT;
        }

        if (this.getPhoto() != null) {
            return Type.PHOTO;
        }

        if (this.getLocation() != null) {
            return Type.LOCATION;
        }

        if (this.getContact() != null) {
            return Type.CONTACT;
        }

        if (this.getText() != null) {
            return Type.TEXT;
        }

        return null;
    }

    public long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(long message_id) {
        this.message_id = message_id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getForward_from() {
        return forward_from;
    }

    public void setForward_from(User forward_from) {
        this.forward_from = forward_from;
    }

    public Chat getForward_from_chat() {
        return forward_from_chat;
    }

    public void setForward_from_chat(Chat forward_from_chat) {
        this.forward_from_chat = forward_from_chat;
    }

    public long getForward_date() {
        return forward_date;
    }

    public void setForward_date(long forward_date) {
        this.forward_date = forward_date;
    }

    public Message getReply_to_message() {
        return reply_to_message;
    }

    public void setReply_to_message(Message reply_to_message) {
        this.reply_to_message = reply_to_message;
    }

    public long getEdit_date() {
        return edit_date;
    }

    public void setEdit_date(long edit_date) {
        this.edit_date = edit_date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<MessageEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<MessageEntity> entities) {
        this.entities = entities;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public List<PhotoSize> getPhoto() {
        return photo;
    }

    public void setPhoto(List<PhotoSize> photo) {
        this.photo = photo;
    }

    public Sticker getSticker() {
        return sticker;
    }

    public void setSticker(Sticker sticker) {
        this.sticker = sticker;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Voice getVoice() {
        return voice;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public User getNew_chat_member() {
        return new_chat_member;
    }

    public void setNew_chat_member(User new_chat_member) {
        this.new_chat_member = new_chat_member;
    }
}
