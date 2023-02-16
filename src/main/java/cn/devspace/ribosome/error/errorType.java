/*
 *
 * .______       __  .______     ______        _______.  ______   .___  ___.  _______
 * |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 * |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 * |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 * |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 * | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 * CreateTime: 2023/1/21
 * Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.error;

public class errorType {



    public static final int Illegal_Parameter = 1001;
    public static final int Callback_Signature_Error = 1002;
    public static final int Callback_Data_Error = 1003;
    public static final int Callback_Login_Token_Error = 1004;
    public static final int Illegal_Permission = 1005;

    public static final int USER_Not_Found = 1006;

    public static final int Database_Error = 1007;

    public static final int APPLICATION_Not_Found = 2001;
    public static final int APPLICATION_Status = 2002;
    public static final int APPLICATION_Already_Applied = 2006;

    public static final int ROLE_Not_Found = 3001;
    public static final int Unknown_Error = -1;
}
