# Bank Token Management

A simple Java Swing GUI application that manages customer tokens in a bank. It is a Data Structures micro project that uses a **Queue (FIFO)** built from scratch with a **linked list**, without relying on Java's built-in collections.

## Problem Statement

In a bank, customers wait for different services. Without a token system, serving customers fairly and keeping track of who is waiting becomes difficult. This program issues a token to each customer and serves them in the order they arrived, separately for each service.

## Objectives

- Issue a unique token number to every customer
- Serve customers in First-Come-First-Served order
- Keep a separate queue for each service
- Let the user search for a token and see its position
- Show all waiting customers at any time

## Data Structure Used

**Queue implemented using a singly linked list.**

- `Token` is a node holding the token number, customer name, service, and a `next` link.
- `TokenQueue` keeps a `front` (next to be served) and a `rear` (last to join) pointer, plus a `size` counter.

**Why a queue?** A bank line is naturally First-In-First-Out, and a linked list lets both enqueue and dequeue run in constant time.

## Operations

| Operation | Method | Description | Time |
|-----------|--------|-------------|------|
| Insert | `enqueue()` | Add a token at the rear | O(1) |
| Delete | `dequeue()` | Remove the token at the front | O(1) |
| Search | `search()` | Linear search for a token number | O(n) |
| Display | `display()` | Show all tokens from front to rear | O(n) |

**Algorithm highlighted:** Linear Search (used to find a token and report its position in the queue).

## Services

1. Cash Deposit
2. Cash Withdrawal
3. Loan Enquiry

Each service has its own queue.

## Project Structure

Everything is in a single file, `SimpleBankToken.java`:

| Class | Purpose |
|-------|---------|
| `Token` | A customer's token and a node of the linked list |
| `TokenQueue` | The custom queue (enqueue, dequeue, search, display) |
| `SimpleBankToken` | The GUI window and button handling |

## How to Run

**Requirements:** JDK 8 or higher.

```bash
javac SimpleBankToken.java
java SimpleBankToken
```

## How to Use

1. **Issue a token:** Type the customer's name, pick a service, and click **Issue Token**.
2. **Serve a customer:** Select a service and click **Call Next**. The first customer in that service's queue is served and removed.
3. **Search a token:** Enter a token number and click **Search** to see which queue it is in and its position.
4. The center panel always shows all three queues and how many customers are waiting in each.

## Sample Test Cases

| # | Action | Expected Result |
|---|--------|-----------------|
| 1 | Issue token with an empty name | "Please enter customer name!" |
| 2 | Issue token for "Ravi" (Cash Deposit) | Token 1 issued, appears in Cash Deposit queue |
| 3 | Issue token for "Anita" (Cash Deposit) | Token 2 added behind Token 1 |
| 4 | Call Next on Cash Deposit | Token 1 (Ravi) is served first (FIFO) |
| 5 | Call Next on an empty queue | "No customers waiting for ..." |
| 6 | Search for token 2 | Found in Cash Deposit queue at position 1 |
| 7 | Search for a served or unissued token | "not found (not issued or already served)" |
| 8 | Search with letters like "abc" | "Enter a valid token number!" |

## Limitations

- Data is stored in memory only, so all tokens are lost when the program closes.
- Fixed set of three services.
- Tokens cannot be cancelled once issued.

## Possible Improvements

- Save and load tokens from a file
- Add or remove services
- Cancel a token or show estimated waiting time
