import { Injectable } from "@angular/core";
import { environment } from "../environments/environments";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Product } from "../models/product";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    private apiUrl = `${environment.apiBaseUrl}/products`;
    constructor(private http: HttpClient) {
        
    }
    getProducts(page: number, limit: number): Observable<Product[]> {
        const params = new HttpParams()
            .set('page', page.toString())
            .set('limit', limit.toString());
        return this.http.get<Product[]>(this.apiUrl, { params });
    }
}