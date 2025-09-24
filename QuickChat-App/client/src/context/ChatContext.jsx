import { createContext, useContext, useEffect, useState } from "react";
import toast from "react-hot-toast";
import { AuthContext } from "./AuthContext";

export const ChatContext = createContext();

export const ChatProvider = ({ children }) => {

  const [messages, setMessages] = useState([]);
  const [users, setUsers] = useState([]);
  // store the id of the user with whom we want to chat
  const [selectedUser, setSelectedUser] = useState(null);
  // in this state we will store userId and with that we will store the number of msgs that is unseen for this particular user. {userId: no. of unseen msgs}
  const [unseenMessages, setUnseenMessages] = useState({});

  const { socket, axios } = useContext(AuthContext);

  // function to get all users for Sidebar
  const getUsers = async () => {
    try {
      const { data } = await axios.get("/api/messages/user");

      if (data.success) {
        setUsers(data.users);
        setUnseenMessages(data.unseenMessages);
      }
    } catch (error) {
      toast.error(error.message);
    }
  }

  // function to get messages for the selected users
  const getMessages = async (userId) => {
    try {
      const { data } = await axios.get(`/api/messages/${userId}`);

      if (data.success) {
        setMessages(data.messages);

        // ✅ Reset unseen count for this user
        setUnseenMessages((prev) => {
          const updated = { ...prev };
          delete updated[userId];
          return updated;
        });
      }
    } catch (error) {
      toast.error(error.message);
    }
  };

  // function to send mesage to the selected user
  const sendMessage = async (messageData) => {
    try {
      const { data } = await axios.post(`/api/messages/send/${selectedUser._id}`, messageData);

      if (data.success) {
        setMessages((prevMessages) => [...prevMessages, data.newMessage])
      } else {
        toast.error(data.message);
      }
    } catch (error) {
      toast.error(error.message);
    }
  }

  // function to subscribe to messages for selected user
  // We will get new Messages instantly
  // If new messages is get by the user then instantly show
  const subscribeToMessages = async () => {
    if (!socket) return;

    socket.on("newMessage", (newMessage) => {
      
      if (selectedUser && newMessage.senderId === selectedUser._id) {
        newMessage.seen = true;
        setMessages((prevMessages) => [...prevMessages, newMessage]);

        // for this particular msg the seen property will be marked as true
        axios.put(`/api/messages/mark/${newMessage._id}`);
      } else {
        setUnseenMessages((prevUnseenMessages) => ({
          ...prevUnseenMessages, [newMessage.senderId]: prevUnseenMessages[newMessage.senderId] ? prevUnseenMessages[newMessage.senderId] + 1 : 1
        }))
      }
    })
  }

  // function to unsubscribe from messages
  const unsubscribeFromMessages = () => {
    if (socket) socket.off("newMessage")
  }

  // Whenever we open the profile page we should execute unsubscribeFromMessages and subscribeToMessages functions
  // whenever the selectedUser changes the below functions will be called
  useEffect(() => {
    subscribeToMessages();

    return () => unsubscribeFromMessages();
  }, [socket, selectedUser])

  const value = {

    messages,
    users,
    selectedUser,
    getUsers,
    getMessages,
    sendMessage,
    setSelectedUser,
    unseenMessages,
    setUnseenMessages

  }

  return (
    <ChatContext.Provider value={value}>
      {children}
    </ChatContext.Provider>
  )

}