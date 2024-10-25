package pt.isel.chimp.service

class ChImpService(){

    val userService: UserService by lazy {
        MockUserService()
    }
}