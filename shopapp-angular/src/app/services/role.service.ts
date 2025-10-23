import { Injectable } from "@angular/core";
import { environment } from "../environments/environments";
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class RoleService {
    private apiUrl = `${environment.apiBaseUrl}/roles`;
    private apiConfig = {
        headers: this.createHeaders(),
    }
    constructor(private http:HttpClient) {}
    private createHeaders(): HttpHeaders {
        return new HttpHeaders({
            'Content-Type': 'application/json',
            'Accept-Language':'vi'
        });

    }
    getRoles() {
        return this.http.get<any[]>(this.apiUrl);
    }
}