import { IsString, IsNotEmpty,IsPhoneNumber,IsDate, IsEmpty, IsNumber  } from "class-validator";

export class LoginDTO {
    @IsPhoneNumber()
    phone_number: string;

    @IsString()
    @IsNotEmpty()
    password: string;

    @IsNumber()
    role_id: number;

    constructor(data: any){
        this.password = data.password;
        this.phone_number = data.phone_number;
        this.role_id = data.role_id;
    }
}