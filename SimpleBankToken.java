import java.awt.*;           // Swing: classes for GUI (JFrame, JButton, JLabel ...)
import java.awt.event.*;     // AWT: layouts like BorderLayout, FlowLayout
import javax.swing.*;        // events: ActionListener, ActionEvent


// TOKEN (one customer waiting in the queue): A token is also a "node" of the linked list, so it stores "next".

class Token {
    int number;        // token number
    String name;       // customer name
    String service;    // service name
    Token next;        // link to the next token in the queue

    Token(int number, String name, String service) {   // constructor
        this.number = number;      
        this.name = name;          
        this.service = service;    
        this.next = null;          
    }
}


// QUEUE (our own queue using a linked list)


class TokenQueue {
    Token front;    // points to the first token
    Token rear;     // points to the last token
    int size;       // how many tokens are waiting

    TokenQueue() {          // constructor
        front = null;
        rear = null;
        size = 0;
    }

    boolean isEmpty() {             
        return front == null;       
    }

    // ENQUEUE : add a token at the REAR (end) of the queue
    void enqueue(Token t) {
        if (rear == null) {         
            front = t;              
            rear = t;               
        } else {                    
            rear.next = t;          
            rear = t;               
        }
        size++;                     
    }

    // DEQUEUE : remove a token from the FRONT of the queue
    Token dequeue() {
        if (front == null) {        
            return null;
        }
        Token removed = front;      
        front = front.next;         
        if (front == null) {        
            rear = null;            
        }
        removed.next = null;        
        size--;                     
        return removed;             
    }

    // SEARCH : look for a token and returns position
    int search(int number) {
        Token temp = front;         
        int position = 1;           
        while (temp != null) {      
            if (temp.number == number) {   
                return position;
            }
            temp = temp.next;       
            position++;             
        }
        return -1;                  // not found
    }

    // DISPLAY :
    String display() {
        if (front == null) {                
            return "   (no customers waiting)\n";
        }
        String text = "";                   
        Token temp = front;                 
        while (temp != null) {              
            text = text + "   Token " + temp.number + " - " + temp.name + "\n";
            temp = temp.next;               
        }
        return text;                        
    }
}


// GUI (the window the user sees):

public class SimpleBankToken extends JFrame implements ActionListener {

    
    String[] services = {"Cash Deposit", "Cash Withdrawal", "Loan Enquiry"};

    
    TokenQueue[] queues = new TokenQueue[3];

    int tokenCounter = 0;      // gives each new customer a new token number

    
    JTextField nameField = new JTextField(10);          // to type customer name
    JComboBox<String> serviceBox = new JComboBox<>(services);   // service dropdown
    JButton issueButton = new JButton("Issue Token");   // adds a customer
    JButton callButton = new JButton("Call Next");      // serves a customer
    JTextField searchField = new JTextField(5);         // to type token number
    JButton searchButton = new JButton("Search");       // searches a token
    JLabel statusLabel = new JLabel("Welcome to the Bank!");   // shows messages
    JTextArea displayArea = new JTextArea();            // shows all queues

    // constructor: builds the window
    public SimpleBankToken() {
        setTitle("Bank Token Management");              // window title
        setSize(600, 450);                              // width, height
        setDefaultCloseOperation(EXIT_ON_CLOSE);        // close button ends program
        setLayout(new BorderLayout());                  

        for (int i = 0; i < 3; i++) {                   // create the 3 queues
            queues[i] = new TokenQueue();
        }

        // TOP panel 
        JPanel topPanel = new JPanel();                 // a panel is a container
        topPanel.add(new JLabel("Name:"));              // label
        topPanel.add(nameField);                        // name text box
        topPanel.add(new JLabel("Service:"));           // label
        topPanel.add(serviceBox);                       // service dropdown
        topPanel.add(issueButton);                      // issue button
        add(topPanel, BorderLayout.NORTH);              // put panel at the top

        //  CENTER :
        displayArea.setEditable(false);                 // user cannot type here
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));   // font
        add(new JScrollPane(displayArea), BorderLayout.CENTER);        // scroll bar

        // BOTTOM panel :
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));   // 2 rows
        JPanel buttonPanel = new JPanel();              // first row
        buttonPanel.add(callButton);                    // call next button
        buttonPanel.add(new JLabel("Token No:"));       // label
        buttonPanel.add(searchField);                   // token number box
        buttonPanel.add(searchButton);                  // search button
        bottomPanel.add(buttonPanel);                   // add first row
        bottomPanel.add(statusLabel);                   // second row = message
        add(bottomPanel, BorderLayout.SOUTH);           // put at the bottom

        // tell Java: when these buttons are clicked, call actionPerformed()
        issueButton.addActionListener(this);
        callButton.addActionListener(this);
        searchButton.addActionListener(this);

        showQueues();                                   // show queues at start
    }

    // this method runs automatically when any button is clicked
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == issueButton) {             // Issue Token clicked
            issueToken();
        } else if (e.getSource() == callButton) {       // Call Next clicked
            callNext();
        } else if (e.getSource() == searchButton) {     // Search clicked
            searchToken();
        }
    }

    // INSERT : create a token and ENQUEUE it
    void issueToken() {
        String name = nameField.getText().trim();       // read name, remove spaces
        if (name.equals("")) {                          // name is empty
            statusLabel.setText("Please enter customer name!");
            return;                                     // stop here
        }
        int index = serviceBox.getSelectedIndex();      // 0, 1 or 2
        tokenCounter++;                                 // next token number
        Token t = new Token(tokenCounter, name, services[index]);   // new token
        queues[index].enqueue(t);                       // add to that service's queue
        statusLabel.setText("Token " + tokenCounter + " issued to " + name
                + " for " + services[index]);           // message
        nameField.setText("");                          // clear the name box
        showQueues();                                   // refresh the display
    }

    // DELETE : DEQUEUE the first customer of the selected service
    void callNext() {
        int index = serviceBox.getSelectedIndex();      // which service
        Token t = queues[index].dequeue();              // remove first token
        if (t == null) {                                // queue was empty
            statusLabel.setText("No customers waiting for " + services[index]);
        } else {
            statusLabel.setText("NOW SERVING: Token " + t.number + " - " + t.name
                    + " (" + t.service + ")");          // message
        }
        showQueues();                                   // refresh the display
    }

    // SEARCH : find a token number in all queues
    void searchToken() {
        int number;
        try {
            number = Integer.parseInt(searchField.getText().trim());   // text -> number
        } catch (NumberFormatException ex) {            // user typed letters
            statusLabel.setText("Enter a valid token number!");
            return;
        }
        for (int i = 0; i < 3; i++) {                   // check each queue
            int position = queues[i].search(number);    // linear search
            if (position != -1) {                       // found
                statusLabel.setText("Token " + number + " is in " + services[i]
                        + " queue at position " + position);
                return;
            }
        }
        statusLabel.setText("Token " + number + " not found (not issued or already served)");
    }

    // DISPLAY : show all three queues in the text area
    void showQueues() {
        String text = "";                               // text to show
        for (int i = 0; i < 3; i++) {                   // for each service
            text = text + services[i] + " (waiting: " + queues[i].size + ")\n";
            text = text + queues[i].display() + "\n";   // tokens of that queue
        }
        displayArea.setText(text);                      // put text on screen
    }

    // main method: program starts here
    public static void main(String[] args) {
        SimpleBankToken window = new SimpleBankToken(); // create the window
        window.setVisible(true);                        // show it
    }
}