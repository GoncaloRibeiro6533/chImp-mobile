package pt.isel.chimp.service.repo

import pt.isel.chimp.domain.Token
import pt.isel.chimp.domain.user.User


class UserRepoMock {

    companion object {
        val users =
            mutableListOf<User>(
                User(1, "Bob", "bob@example.com"),
                User(2, "Alice", "alice@example.com"),
                User(3, "John", "john@example.com"),
            )

        val passwords = mutableMapOf(
            1 to "A1234ab",
            2 to "1234VDd",
            3 to "1234SADfs",
        )
        private var currentId = 4


        private val sessions = mutableListOf<Token>(
            Token("token1", 1),
        )
    }

    fun createUser(username: String, email: String, password: String): User {
        val user = User(currentId++, username, email)
        users.add(user)
        passwords[user.id] = password
        return user
    }

    fun findSessionByToken(token: String): Token? {
        return sessions.find { it.token == token }
    }

    fun findUserByUsername(username: String, limit: Int = 10, skip: Int = 0): List<User> {
        return users.filter { it.username.contains(username) }
            .drop(skip)
            .take(limit)
    }

    fun updateUser(id: Int, newUsername: String): User {
        val user = users.find { it.id == id }!!
        val newUser = user.copy(username = newUsername)
        users.remove(user)
        users.add(newUser)
        return newUser
    }

    fun findUserById(id: Int): User? {
        return users.find { it.id == id }
    }

    fun findUserByPassword(id: Int, password: String): User? {
        return if (passwords[id] == password) users.find { it.id == id } else null
    }

    fun createSession(userId: Int): Token {
        val token = "token${sessions.size + 1}"
        val session = Token(token, userId)
        sessions.add(session)
        return session
    }
    fun findByEmail(email: String): User? {
        return users.find { it.email == email }
    }

    fun deleteSession(session: Token) {
        sessions.remove(session)
    }

    /*fun deleteAllSessions() {
        sessions.clear()
    }/*TODO: Implement this method*/
    */
}