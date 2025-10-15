import { IsString, IsNotEmpty,IsPhoneNumber,IsDate, IsEmpty  } from "class-validator";

export class LoginDTO {
    @IsPhoneNumber()
    phone_number: string;

    @IsString()
    @IsNotEmpty()
    password: string;

    constructor(data: any){
        this.password = data.password;
        this.phone_number = data.phone_number;
    }
}