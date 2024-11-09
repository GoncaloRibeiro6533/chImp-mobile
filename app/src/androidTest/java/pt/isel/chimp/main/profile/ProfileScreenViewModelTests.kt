package pt.isel.chimp.main.profile

/*
@OptIn(ExperimentalCoroutinesApi::class)
class ProfileScreenViewModelTests {

    @get:Rule
    val dispatcherRule = ReplaceMainDispatcherRule()

    @Test
    fun fetch_user_profile_transitions_to_loading_state()  = runTest(dispatcherRule.testDispatcher){
        // Arrange
        val viewModel = ProfileScreenViewModel(MockUserService())
        // Act
        viewModel.fetchProfile("token1")
        // Assert
        assert(viewModel.state is ProfileScreenState.Loading, { "Expected state to be Loading  but was ${viewModel.state}" })
    }

    @Test
    fun fetch_user_profile_transitions_to_success_state()  = runTest(dispatcherRule.testDispatcher){
        // Arrange
        val viewModel = ProfileScreenViewModel(MockUserService())
        // Act
        viewModel.fetchProfile("token1")
        advanceUntilIdle()
        // Assert
        assert(viewModel.state is ProfileScreenState.Success, { "Expected state to be Success  but was ${viewModel.state}" })
    }

    @Test
    fun fetch_user_profile_transitions_to_error_state()  = runTest(dispatcherRule.testDispatcher){
        // Arrange
        val viewModel = ProfileScreenViewModel(MockUserService())
        // Act
        viewModel.fetchProfile("invalid_token")
        advanceUntilIdle()
        // Assert
        assert(viewModel.state is ProfileScreenState.Error, { "Expected state to be Error  but was ${viewModel.state}" })
    }

    @Test
    fun edit_username_transitions_to_loading_state()  = runTest(dispatcherRule.testDispatcher){
        // Arrange
        val viewModel = ProfileScreenViewModel(MockUserService())
        // Act
        viewModel.editUsername("new_username", "token1")
        // Assert
        assert(viewModel.state is ProfileScreenState.Loading, { "Expected state to be Loading  but was ${viewModel.state}" })
    }

    @Test
    fun edit_username_transitions_to_success_state()  = runTest(dispatcherRule.testDispatcher){
        // Arrange
        val viewModel = ProfileScreenViewModel(MockUserService())
        // Act
        viewModel.editUsername("new_username", "token1")
        advanceUntilIdle()
        // Assert
        assert(viewModel.state is ProfileScreenState.Success, { "Expected state to be Success  but was ${viewModel.state}" })
    }

    @Test
    fun edit_username_transitions_to_error_state()  = runTest(dispatcherRule.testDispatcher){
        // Arrange
        val viewModel = ProfileScreenViewModel(MockUserService())
        // Act
        viewModel.editUsername("new_username", "invalid_token")
        advanceUntilIdle()
        // Assert
        assert(viewModel.state is ProfileScreenState.Error, { "Expected state to be Error  but was ${viewModel.state}" })
    }

    @Test
    fun set_edit_state()  = runTest(dispatcherRule.testDispatcher){
        // Arrange
        val viewModel = ProfileScreenViewModel(MockUserService())
        // Act
        viewModel.setEditState(Profile("username", "email"))
        // Assert
        assert(viewModel.state is ProfileScreenState.EditingUsername, { "Expected state to be EditingUsername  but was ${viewModel.state}" })
    }

}*/